package it.pasqualecavallo.videowatch.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.pasqualecavallo.videowatch.controller.dto.LabelValue;
import it.pasqualecavallo.videowatch.controller.dto.SeasonBean;
import it.pasqualecavallo.videowatch.controller.dto.SeriesBean;
import it.pasqualecavallo.videowatch.controller.dto.SubmitEpisodeBean;
import it.pasqualecavallo.videowatch.controller.dto.SubmitEpisodeResponseBean;
import it.pasqualecavallo.videowatch.controller.dto.SubmitFilmBean;
import it.pasqualecavallo.videowatch.controller.dto.SubmitFilmResponseBean;
import it.pasqualecavallo.videowatch.model.Episode;
import it.pasqualecavallo.videowatch.model.Film;
import it.pasqualecavallo.videowatch.model.Season;
import it.pasqualecavallo.videowatch.model.Serie;
import it.pasqualecavallo.videowatch.repo.EpisodeRepository;
import it.pasqualecavallo.videowatch.repo.FilmRepository;
import it.pasqualecavallo.videowatch.repo.SeasonRepository;
import it.pasqualecavallo.videowatch.repo.SerieRepository;
import it.pasqualecavallo.videowatch.util.MultimediaUtils;
import it.pasqualecavallo.videowatch.util.StorageUtils;

@Service
public class ManageService {

	@Autowired
	private SerieRepository serieRepo;

	@Autowired
	private SeasonRepository seasonRepo;

	@Autowired
	private EpisodeRepository episodeRepo;

	@Autowired
	private FilmRepository filmRepo;

	@Autowired
	private StorageUtils storageUtils;

	@Autowired
	private MultimediaUtils mmUtils;
	
	private static final Logger logger = LoggerFactory.getLogger(ManageService.class);
	
	@Transactional
	public SeriesBean persistNewSeries(SeriesBean bean) {
		Serie s = new Serie();
		s.setDescription(bean.getDescription());
		s.setImage(bean.getImage());
		s.setName(bean.getTitle());
		s.setRank(bean.getRank());
		s.setTags(bean.getTags());
		s = serieRepo.save(s);
		bean.setDescription(s.getDescription());
		bean.setImage(s.getImage());
		bean.setTitle(s.getName());
		bean.setTags(s.getTags());
		bean.setRank(s.getRank());
		bean.setId(s.getId());
		logger.info("Creata nuova serie: "+bean.getTitle());
		return bean;
	}

	@Transactional
	public SeasonBean persistNewSeason(@Valid SeasonBean season) {
		Optional<Serie> oserie = serieRepo.findById(season.getIdSerie());
		if (!oserie.isPresent()) {
			SeasonBean sb = new SeasonBean();
			sb.setErrorMessage("INVALID_SERIE");
			logger.info("La serie non esiste: "+season.getIdSerie());
			return sb;
		}
		Season checkSeason = new Season();
		checkSeason.setSerie(oserie.get());
		checkSeason.setNumber(season.getNumber());
		Example<Season> es = Example.of(checkSeason);
		if (!seasonRepo.findAll(es).isEmpty()) {
			logger.info("La stagione è già presente: "+season.getNumber());
			SeasonBean sb = new SeasonBean();
			sb.setErrorMessage("ALREDY_EXISTS");
			return sb;
		}
		Season s = new Season();
		s.setDescription(season.getDescription());
		s.setImage(season.getImage());
		s.setNumber(season.getNumber());
		s.setSerie(oserie.get());
		s = seasonRepo.save(s);
		season.setIdSeason(s.getId());
		logger.info("Creata nuova stagione:"+s.getId());
		return season;
	}

	@Transactional
	public SubmitEpisodeResponseBean uploadEpisode(MultipartFile file, @Valid SubmitEpisodeBean episode) {
		Optional<Season> oseason = seasonRepo.findById(episode.getIdSeason());
		Optional<Serie> oserie = serieRepo.findById(episode.getIdSerie());
		if (!oseason.isPresent() || !oserie.isPresent()) {
			logger.info("Serie o stagione non trovata");
			SubmitEpisodeResponseBean response = new SubmitEpisodeResponseBean();
			response.setErrorMessage("BAD_REQUEST");
			return response;
		}
		Integer id = oseason.get().getSerie().getId();
		Integer id2 = oserie.get().getId();
		if (!(id.equals(id2))) {
			logger.info("La stagione non appartiene alla serie");
			SubmitEpisodeResponseBean response = new SubmitEpisodeResponseBean();
			response.setErrorMessage("BAD_REQUEST");
			return response;
		}
		Episode testEpisode = new Episode();
		testEpisode.setNumber(episode.getNumber());
		testEpisode.setSeason(oseason.get());
		Example<Episode> eEpisode = Example.of(testEpisode);
		Optional<Episode> oEpisode = episodeRepo.findOne(eEpisode);
		if (oEpisode.isPresent()) {
			logger.info("L' episodio è già presente: "+episode.getNumber());
			SubmitEpisodeResponseBean response = new SubmitEpisodeResponseBean();
			response.setErrorMessage("ALREDY_PRESENT");
			return response;
		}
		String buildFileName = oserie.get().getName().replaceAll("[^A-Za-z0-9\\s]", "") + "-"
				+ oseason.get().getNumber() + "x" + episode.getNumber() + " - "
				+ episode.getTitle().replaceAll("[^A-Za-z0-9\\s]", "");
		try {
			String storedTo = storageUtils.persistFile(file, buildFileName);
			String saveImageTo = mmUtils.getAndConvertImageFromVideo(storedTo);
			Episode episodeEntity = new Episode();
			episodeEntity.setDescription(episode.getDescription());
			episodeEntity.setSeason(oseason.get());
			episodeEntity.setNumber(episode.getNumber());
			episodeEntity.setRank(episode.getRank());
			episodeEntity.setTitle(episode.getTitle());
			episodeEntity.setDate(System.currentTimeMillis());
			episodeEntity.setImage(saveImageTo);
//			episodeEntity.setMime();
			episodeEntity.setPath(storedTo);
			episodeEntity = episodeRepo.save(episodeEntity);
			SubmitEpisodeResponseBean response = new SubmitEpisodeResponseBean();
			response.setDescription(episode.getDescription());
			response.setIdEpisode(episodeEntity.getId());
			response.setIdSeason(episode.getIdSeason());
			response.setIdSerie(episode.getIdSerie());
			response.setNumber(episode.getNumber());
			response.setRank(episode.getRank());
			response.setTitle(episode.getTitle());
			response.setTime(episodeEntity.getDate());
			return response;
		} catch (Exception e) {
			logger.info("Si è verificato un problema nel caricamento",e);
			SubmitEpisodeResponseBean response = new SubmitEpisodeResponseBean();
			response.setErrorMessage("EXCEPTION");
			return response;
		}
	}

	@Transactional
	public SubmitFilmResponseBean uploadFilm(MultipartFile file, @Valid SubmitFilmBean request) {
		String storedTo;
		String saveImageTo;
		try {
			storedTo = storageUtils.persistFile(file, request.getTitle());
			saveImageTo = mmUtils.getAndConvertImageFromVideo(storedTo);
		} catch (IOException e) {
			logger.info("Si è verificato un problema",e);
			SubmitFilmResponseBean response = new SubmitFilmResponseBean();
			response.setErrorMessage("EXCEPTION");
			return response;
		}
		Film film = new Film();
		film.setDescription(request.getDescription());
		film.setPath(storedTo);
		film.setRank(request.getRank());
		film.setTags(request.getTags());
		film.setTitle(request.getTitle());
		film.setType(request.getType());
		film.setUploadDate(System.currentTimeMillis());
		film.setImage(saveImageTo);
		film = filmRepo.save(film);
		SubmitFilmResponseBean response = new SubmitFilmResponseBean();
		response.setDescription(film.getDescription());
		response.setIdFilm(film.getId());
		response.setRank(film.getRank());
		response.setTags(film.getTags());
		response.setTime(film.getUploadDate());
		response.setTitle(film.getTitle());
		response.setType(film.getType());
		return response;
	}

	@Transactional
	public List<LabelValue> searchSerie(String serie) {
		List<Serie> series = serieRepo.findByNameContaining(serie);
		List<LabelValue> list = new ArrayList<>();
		for (Serie s : series) {
			LabelValue e = new LabelValue();
			e.setValue(s.getId());
			e.setLabel(s.getName());
			list.add(e);
		}
		return list;
	}

	@Transactional
	public List<LabelValue> searchSeason(Integer idSerie) {
		List<LabelValue> list = new ArrayList<>();
		Season season = new Season();
		Serie serie = new Serie();
		serie.setId(idSerie);
		season.setSerie(serie);
		Example<Season> eSeason = Example.of(season);
		List<Season> seasons = seasonRepo.findAll(eSeason);
		for (Season s : seasons) {
			LabelValue lv = new LabelValue();
			lv.setLabel(s.getId().toString());
			lv.setValue(s.getNumber());
			list.add(lv);
		}
		return list;
	}


}

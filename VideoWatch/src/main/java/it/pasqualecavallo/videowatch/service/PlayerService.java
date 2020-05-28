package it.pasqualecavallo.videowatch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.pasqualecavallo.videowatch.controller.dto.FilmItem;
import it.pasqualecavallo.videowatch.controller.dto.GetEpisodeItemBean;
import it.pasqualecavallo.videowatch.controller.dto.GetEpisodeResponseBean;
import it.pasqualecavallo.videowatch.controller.dto.GetFilmsResponseBean;
import it.pasqualecavallo.videowatch.controller.dto.GetSeasonResponseBean;
import it.pasqualecavallo.videowatch.controller.dto.GetSeriesResponseBean;
import it.pasqualecavallo.videowatch.controller.dto.SeasonBean;
import it.pasqualecavallo.videowatch.controller.dto.SeriesBean;
import it.pasqualecavallo.videowatch.model.Episode;
import it.pasqualecavallo.videowatch.model.Film;
import it.pasqualecavallo.videowatch.model.Season;
import it.pasqualecavallo.videowatch.model.Serie;
import it.pasqualecavallo.videowatch.repo.EpisodeRepository;
import it.pasqualecavallo.videowatch.repo.FilmRepository;
import it.pasqualecavallo.videowatch.repo.SeasonRepository;
import it.pasqualecavallo.videowatch.repo.SerieRepository;

@Service
public class PlayerService {

	private static final String getEpisode = "SELECT e.* FROM Episode e WHERE e.season_id=?";

	@Autowired
	private SeasonRepository seasonRepo;

	@Autowired
	private FilmRepository filmRepo;

	@Autowired
	private SerieRepository serieRepo;

	@Autowired
	private EpisodeRepository episodeRepo;

	@Autowired
	private EntityManager entityManager;

	@Transactional
	public GetSeasonResponseBean getSeasonBySerie(Integer idSerie) {
		Season season = new Season();
		Serie serie = new Serie();
		serie.setId(idSerie);
		season.setSerie(serie);
		Example<Season> eSeason = Example.of(season);
		List<Season> seasons = seasonRepo.findAll(eSeason);
		GetSeasonResponseBean response = new GetSeasonResponseBean();
		List<SeasonBean> list = new ArrayList<>();
		for (Season s : seasons) {
			SeasonBean bean = new SeasonBean();
			bean.setDescription(s.getDescription());
			bean.setIdSeason(s.getId());
			bean.setIdSerie(s.getSerie().getId());
			bean.setImage(s.getImage());
			bean.setNumber(s.getNumber());
			list.add(bean);
		}
		response.setSeasons(list);
		return response;
	}

	@Transactional
	public GetFilmsResponseBean findByTitle(String title) {
		List<Film> films = filmRepo.findByNameContaining(title);
		GetFilmsResponseBean response = new GetFilmsResponseBean();
		List<FilmItem> list = new ArrayList<>();
		for (Film f : films) {
			FilmItem item = new FilmItem();
			item.setDescription(f.getDescription());
			item.setId(f.getId());
			item.setLastView(f.getLastView());
			item.setRank(f.getRank());
			item.setTags(f.getTags());
			item.setTitle(f.getTitle());
			item.setType(f.getType());
			item.setUploadDate(f.getUploadDate());
			item.setImage(f.getImage());
			list.add(item);
		}
		response.setFilms(list);
		return response;
	}

	@Transactional
	public GetSeriesResponseBean getSeries(String serie) {
		List<Serie> series;
		if (serie == null || serie.length() == 0) {
			series = serieRepo.findAll();
		} else {
			Serie example = new Serie();
			example.setName(serie);
			series = serieRepo.findByNameContaining(serie);
		}
		GetSeriesResponseBean response = new GetSeriesResponseBean();
		List<SeriesBean> list = new ArrayList<>();
		for (Serie s : series) {
			SeriesBean bean = new SeriesBean();
			bean.setDescription(s.getDescription());
			bean.setId(s.getId());
			bean.setImage(s.getImage());
			bean.setRank(s.getRank());
			bean.setTags(s.getTags());
			bean.setTitle(s.getName());
			list.add(bean);
		}
		response.setSeries(list);
		return response;
	}

	@Transactional
	public GetEpisodeResponseBean getEpisodeBySeason(Integer idSeason) {
		Query q = entityManager.createNativeQuery(getEpisode, Episode.class);
		q.setParameter(1, idSeason);
		@SuppressWarnings("unchecked")
		List<Episode> list = q.getResultList();
		GetEpisodeResponseBean response = new GetEpisodeResponseBean();
		List<GetEpisodeItemBean> listBean = new ArrayList<>();
		for (Episode e : list) {
			GetEpisodeItemBean item = new GetEpisodeItemBean();
			item.setDate(e.getDate());
			item.setDescription(e.getDescription());
			item.setId(e.getId());
			item.setLastView(e.getLastView());
			item.setNumber(e.getNumber());
			item.setRank(e.getRank());
			item.setTitle(e.getTitle());
			item.setImage(e.getImage());
			listBean.add(item);
		}
		response.setList(listBean);
		return response;
	}

	@Transactional
	public void viewEpisode(Integer id, Integer isFilm) {
		if (isFilm == 1) {
			Optional<Film> of = filmRepo.findById(id);
			if (of.isPresent()) {
				Film f = of.get();
				f.setLastView(System.currentTimeMillis());
				filmRepo.save(f);
			}
		} else {
			Optional<Episode> oe = episodeRepo.findById(id);
			if (oe.isPresent()) {
				Episode e = oe.get();
				e.setLastView(System.currentTimeMillis());
				episodeRepo.save(e);
			}
		}
	}

	public String findFile(String isFilm, String id) {
		if (isFilm.equals("true")) {
			Film f = new Film();
			f.setId(new Integer(id));
			Optional<Film> of = filmRepo.findById(new Integer(id));
			if (of.isPresent()) {
				return of.get().getPath();
			}
			return null;
		} else {
			Episode e = new Episode();
			e.setId(new Integer(id));
			Optional<Episode> oe = episodeRepo.findById(new Integer(id));
			if (oe.isPresent()) {
				return oe.get().getPath();
			}
			return null;
		}
	}

	@Transactional
	public GetFilmsResponseBean getFilms() {
		List<Film> films = filmRepo.findAll();
		GetFilmsResponseBean response = new GetFilmsResponseBean();
		List<FilmItem> list = new ArrayList<>();
		for (Film film : films) {
			FilmItem item = new FilmItem();
			item.setDescription(film.getDescription());
			item.setId(film.getId());
			item.setLastView(film.getLastView());
			item.setRank(film.getRank());
			item.setTags(film.getTags());
			item.setTitle(film.getTitle());
			item.setType(film.getType());
			item.setUploadDate(film.getUploadDate());
			item.setImage(film.getImage());
			list.add(item);
		}
		response.setFilms(list);
		return response;
	}

}

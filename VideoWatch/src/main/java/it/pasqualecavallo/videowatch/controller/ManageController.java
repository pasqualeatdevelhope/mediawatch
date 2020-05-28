package it.pasqualecavallo.videowatch.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import it.pasqualecavallo.videowatch.controller.dto.LabelValue;
import it.pasqualecavallo.videowatch.controller.dto.SeasonBean;
import it.pasqualecavallo.videowatch.controller.dto.SeriesBean;
import it.pasqualecavallo.videowatch.controller.dto.SubmitEpisodeBean;
import it.pasqualecavallo.videowatch.controller.dto.SubmitEpisodeResponseBean;
import it.pasqualecavallo.videowatch.controller.dto.SubmitFilmBean;
import it.pasqualecavallo.videowatch.controller.dto.SubmitFilmResponseBean;
import it.pasqualecavallo.videowatch.service.ManageService;

@Controller
@CrossOrigin("*")
public class ManageController {

	@Autowired
	private ManageService manageService;

	@PostMapping(value = "createserie")
	public @ResponseBody SeriesBean createSeries(@RequestBody @Valid SeriesBean serie) {
		return manageService.persistNewSeries(serie);
	}

	@PostMapping(value = "createseason")
	public @ResponseBody SeasonBean createSeason(@RequestBody @Valid SeasonBean season) {
		return manageService.persistNewSeason(season);
	}

	@PostMapping(value = "submitepisode", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public @ResponseBody SubmitEpisodeResponseBean createEpisode(@RequestParam(value = "file") MultipartFile file,
			@RequestParam Integer idSeason, @RequestParam Integer idSerie, @RequestParam Integer number,
			@RequestParam String title, @RequestParam(required = false) String description,
			@RequestParam(required = false) Integer rank) {
		SubmitEpisodeBean episode = new SubmitEpisodeBean();
		episode.setDescription(description);
		episode.setIdSeason(idSeason);
		episode.setIdSerie(idSerie);
		episode.setNumber(number);
		episode.setRank(rank);
		episode.setTitle(title);
		return manageService.uploadEpisode(file, episode);
	}

	@PostMapping(value = "submitfilm", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public @ResponseBody SubmitFilmResponseBean createFilm(@RequestParam MultipartFile file,
			@RequestParam(required = false) String tags, @RequestParam String title,
			@RequestParam(required = false) String type, @RequestParam(required = false) String description,
			@RequestParam(required = false) Integer rank) {
		SubmitFilmBean episode = new SubmitFilmBean();
		episode.setDescription(description);
		episode.setRank(rank);
		episode.setTags(tags);
		episode.setTitle(title);
		episode.setType(type);
		return manageService.uploadFilm(file, episode);
	}

	@GetMapping(value = "searchserie")
	public @ResponseBody List<LabelValue> searchSerie(@RequestParam String serie) {
		return manageService.searchSerie(serie);
	}
	
	@GetMapping(value="searchseason")
	public @ResponseBody List<LabelValue> searchSeason(@RequestParam Integer idSerie){
		return manageService.searchSeason(idSerie);
	}
	

}
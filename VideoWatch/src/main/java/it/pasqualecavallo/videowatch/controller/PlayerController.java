package it.pasqualecavallo.videowatch.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.pasqualecavallo.videowatch.controller.dto.GetEpisodeResponseBean;
import it.pasqualecavallo.videowatch.controller.dto.GetFilmsResponseBean;
import it.pasqualecavallo.videowatch.controller.dto.GetSeasonResponseBean;
import it.pasqualecavallo.videowatch.controller.dto.GetSeriesResponseBean;
import it.pasqualecavallo.videowatch.service.PlayerService;
import it.pasqualecavallo.videowatch.util.MultipartFileSender;

@Controller
public class PlayerController {

	@Autowired
	private PlayerService playerService;

	@GetMapping(value = "getseasonbyserie")
	public @ResponseBody GetSeasonResponseBean getSeasonBySerie(@RequestParam Integer idSerie) {
		return playerService.getSeasonBySerie(idSerie);
	}

	@GetMapping(value = "getepisodebyseason")
	public @ResponseBody GetEpisodeResponseBean getEpisodeBySeason(@RequestParam Integer idSeason) {
		return playerService.getEpisodeBySeason(idSeason);
	}

	@GetMapping(value = "searchfilmbytitle")
	public @ResponseBody GetFilmsResponseBean searchFilmByTitle(@RequestParam String title) {
		return playerService.findByTitle(title);
	}

	@GetMapping(value = "getseries")
	public @ResponseBody GetSeriesResponseBean getSeries(@RequestParam(required = false) String serie) {
		return playerService.getSeries(serie);
	}

	@GetMapping(value = "getfilms")
	public @ResponseBody GetFilmsResponseBean getFilms() {
		return playerService.getFilms();
	}
	
	@GetMapping(value = "updatelastview")
	public @ResponseBody String updatelastview(@RequestParam Integer id, @RequestParam Integer isFilm) {
		playerService.viewEpisode(id, isFilm);
		return "OK";
	}

	@GetMapping(value = "stream")
	public void stream(@RequestParam String isFilm, @RequestParam String id,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		String file = playerService.findFile(isFilm, id);
		try {
			MultipartFileSender.fromFile(new File(file)).with(httpServletRequest).with(httpServletResponse)
					.serveResource();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

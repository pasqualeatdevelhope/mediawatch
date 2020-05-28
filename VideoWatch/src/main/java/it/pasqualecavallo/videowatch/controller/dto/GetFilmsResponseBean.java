package it.pasqualecavallo.videowatch.controller.dto;

import java.util.List;

public class GetFilmsResponseBean {

	private List<FilmItem> films;

	public List<FilmItem> getFilms() {
		return films;
	}

	public void setFilms(List<FilmItem> films) {
		this.films = films;
	}

}

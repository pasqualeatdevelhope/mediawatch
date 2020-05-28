package it.pasqualecavallo.videowatch.controller.dto;

public class SubmitFilmResponseBean extends SubmitFilmBean {

	private Long time;

	private Integer idFilm;

	private String errorMessage;

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getIdFilm() {
		return idFilm;
	}

	public void setIdFilm(Integer idFilm) {
		this.idFilm = idFilm;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}

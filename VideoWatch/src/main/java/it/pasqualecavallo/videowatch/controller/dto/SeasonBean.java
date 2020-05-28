package it.pasqualecavallo.videowatch.controller.dto;

import javax.validation.constraints.NotNull;

public class SeasonBean {

	@NotNull
	private Integer idSerie;

	private Integer idSeason;

	private String description;

	@NotNull
	private Integer number;

	private String image;

	private String errorMessage;

	public Integer getIdSerie() {
		return idSerie;
	}

	public void setIdSerie(Integer idSerie) {
		this.idSerie = idSerie;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getIdSeason() {
		return idSeason;
	}

	public void setIdSeason(Integer idSeason) {
		this.idSeason = idSeason;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}

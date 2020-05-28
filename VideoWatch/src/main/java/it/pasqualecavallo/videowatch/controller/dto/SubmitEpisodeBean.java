package it.pasqualecavallo.videowatch.controller.dto;

public class SubmitEpisodeBean {

	private Integer idSeason;
	private Integer idSerie;
	private Integer number;
	private String title;
	private String description;
	private Integer rank;
	
	public Integer getIdSeason() {
		return idSeason;
	}
	public void setIdSeason(Integer idSeason) {
		this.idSeason = idSeason;
	}
	public Integer getIdSerie() {
		return idSerie;
	}
	public void setIdSerie(Integer idSerie) {
		this.idSerie = idSerie;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	
}

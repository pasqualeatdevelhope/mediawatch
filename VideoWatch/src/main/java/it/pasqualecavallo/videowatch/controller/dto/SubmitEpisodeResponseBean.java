package it.pasqualecavallo.videowatch.controller.dto;

public class SubmitEpisodeResponseBean extends SubmitEpisodeBean{

	private String errorMessage;
	
	private Integer idEpisode;

	private Long time;
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Integer getIdEpisode() {
		return idEpisode;
	}

	public void setIdEpisode(Integer idEpisode) {
		this.idEpisode = idEpisode;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
	
}

package it.pasqualecavallo.videowatch.controller.dto;

import javax.validation.constraints.NotEmpty;

public class SubmitFilmBean {

	private String tags;

	@NotEmpty
	private String title;
	private String description;
	private String type;
	private Integer rank;

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

}

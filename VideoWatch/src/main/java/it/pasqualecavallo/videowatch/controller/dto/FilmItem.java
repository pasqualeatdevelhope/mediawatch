package it.pasqualecavallo.videowatch.controller.dto;

public class FilmItem {
	private String description;
	private Integer id;
	private Long lastView;
	private Integer rank;
	private String tags;
	private String title;
	private String type;
	private Long uploadDate;
	private String image;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getLastView() {
		return lastView;
	}

	public void setLastView(Long lastView) {
		this.lastView = lastView;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Long uploadDate) {
		this.uploadDate = uploadDate;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}
}

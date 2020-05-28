package it.pasqualecavallo.videowatch.controller.dto;

public class GetEpisodeItemBean {
	private Long date;
	private String description;
	private Integer id;
	private Long lastView;
	private Integer number;
	private Integer rank;
	private String title;
	private String image;
	
	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getImage() {
		return image;
	}

}

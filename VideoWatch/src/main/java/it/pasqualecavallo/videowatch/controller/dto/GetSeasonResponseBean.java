package it.pasqualecavallo.videowatch.controller.dto;

import java.util.List;

public class GetSeasonResponseBean {

	private List<SeasonBean> seasons;

	public List<SeasonBean> getSeasons() {
		return seasons;
	}

	public void setSeasons(List<SeasonBean> seasons) {
		this.seasons = seasons;
	}
	
}

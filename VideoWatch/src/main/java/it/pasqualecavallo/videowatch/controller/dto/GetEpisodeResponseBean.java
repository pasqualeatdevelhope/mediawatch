package it.pasqualecavallo.videowatch.controller.dto;

import java.util.List;

public class GetEpisodeResponseBean {
	List<GetEpisodeItemBean> list;

	public void setList(List<GetEpisodeItemBean> list) {
		this.list = list;
	}

	public List<GetEpisodeItemBean> getList() {
		return list;
	}
}

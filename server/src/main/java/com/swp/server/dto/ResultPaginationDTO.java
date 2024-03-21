package com.swp.server.dto;

import java.util.ArrayList;
import java.util.List;

public class ResultPaginationDTO<T> {
	private int page;
	private int totalPage;
	private List<T> listResults = new ArrayList<>();

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<T> getListResults() {
		return listResults;
	}

	public void setListResults(List<T> listResults) {
		this.listResults = listResults;
	}

}

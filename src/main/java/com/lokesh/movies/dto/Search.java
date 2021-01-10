package com.lokesh.movies.dto;

public class Search {

	private String searchName;
	private String type;

	public Search() {
		super();
	}

	public Search(String searchName) {
		super();
		this.searchName = searchName;
	}

	public Search(String searchName, String type) {
		super();
		this.searchName = searchName;
		this.type = type;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

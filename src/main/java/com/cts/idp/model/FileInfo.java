package com.cts.idp.model;

public class FileInfo {
	private String name;
	private String url;
	private String deleteUrl;

	public FileInfo(String name, String url, String deleteUrl) {
		this.name = name;
		this.url = url;
		this.deleteUrl = deleteUrl;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

}

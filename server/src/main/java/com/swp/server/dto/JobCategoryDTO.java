package com.swp.server.dto;

import org.springframework.web.multipart.MultipartFile;

public class JobCategoryDTO {

	private String name;
	private MultipartFile image;

	public JobCategoryDTO() {
		// TODO Auto-generated constructor stub
	}

	public JobCategoryDTO(String name, MultipartFile image) {
		super();

		this.name = name;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

}

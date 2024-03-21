package com.swp.server.dto;

import org.springframework.web.multipart.MultipartFile;

public class UpdateJobCategoryDTO {
	private int id;
	private String name;
	private MultipartFile image;

	public UpdateJobCategoryDTO() {
		// TODO Auto-generated constructor stub
	}

	public UpdateJobCategoryDTO(int id, String name, MultipartFile image) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

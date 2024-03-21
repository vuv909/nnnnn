package com.swp.server.dto;

import java.util.Date;

public class JobCateDTO {
	private int id;
	private String name;
	private byte[] image;
	private Date create_At;
	private Date update_At;

	public JobCateDTO() {
		// TODO Auto-generated constructor stub
	}

	public JobCateDTO(int id, String name, byte[] image, Date create_At, Date update_At) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.create_At = create_At;
		this.update_At = update_At;
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

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Date getCreate_At() {
		return create_At;
	}

	public void setCreate_At(Date create_At) {
		this.create_At = create_At;
	}

	public Date getUpdate_At() {
		return update_At;
	}

	public void setUpdate_At(Date update_At) {
		this.update_At = update_At;
	}

}

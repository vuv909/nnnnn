package com.swp.server.dto;

public class BlockAccDTO {

	private String email;

	public BlockAccDTO() {
		// TODO Auto-generated constructor stub
	}

	public BlockAccDTO(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "BlockAccDTO [email=" + email + "]";
	}

}

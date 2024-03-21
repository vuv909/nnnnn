package com.swp.server.dto;

public class ChangePasswordDTO {

	private String email;
	private String password;

	public ChangePasswordDTO() {
		// TODO Auto-generated constructor stub
	}

	public ChangePasswordDTO(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

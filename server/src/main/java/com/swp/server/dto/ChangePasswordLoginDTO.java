package com.swp.server.dto;

public class ChangePasswordLoginDTO {
	private String email;
	private String password;
	private String oldPassword;

	public ChangePasswordLoginDTO() {
		// TODO Auto-generated constructor stub
	}

	public ChangePasswordLoginDTO(String email, String password, String oldPassword) {
		super();
		this.email = email;
		this.password = password;
		this.oldPassword = oldPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

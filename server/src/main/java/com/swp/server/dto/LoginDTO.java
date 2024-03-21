package com.swp.server.dto;

public class LoginDTO {

	private String email;
	private String password;
	private boolean remember;

	public LoginDTO() {
		// TODO Auto-generated constructor stub
	}

	public LoginDTO(String email, String password, boolean remember) {
		super();
		this.email = email;
		this.password = password;
		this.remember = remember;
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

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}

}

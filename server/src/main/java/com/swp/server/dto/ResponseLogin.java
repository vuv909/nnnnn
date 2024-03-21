package com.swp.server.dto;

public class ResponseLogin {

	private String jwt;
	private int id;
	private String email;
	private boolean enable;

	public ResponseLogin() {
		// TODO Auto-generated constructor stub
	}

	public ResponseLogin(String jwt, int id, String email) {
		super();
		this.jwt = jwt;
		this.id = id;
		this.email = email;
	}

	public String getJwt() {
		return jwt;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

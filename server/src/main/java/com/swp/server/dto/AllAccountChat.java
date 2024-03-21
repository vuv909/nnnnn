package com.swp.server.dto;

public class AllAccountChat {

	private int id;
	private String email;
	private int accountId;
	private byte[] avatar;

	public AllAccountChat() {
		// TODO Auto-generated constructor stub
	}

	public AllAccountChat(int id, String email, byte[] avatar) {
		super();
		this.id = id;
		this.email = email;
		this.avatar = avatar;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

}

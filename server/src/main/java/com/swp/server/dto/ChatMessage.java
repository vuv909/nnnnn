package com.swp.server.dto;

public class ChatMessage {

	String message;
	String user;

	public ChatMessage() {
		// TODO Auto-generated constructor stub
	}

	public ChatMessage(String message, String user) {
		super();
		this.message = message;
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "ChatMessage [message=" + message + ", user=" + user + "]";
	}

}

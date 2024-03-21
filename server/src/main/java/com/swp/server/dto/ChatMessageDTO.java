package com.swp.server.dto;

public class ChatMessageDTO {
	private String message;
	private String user;

	// Constructor, getters, and setters
	public ChatMessageDTO() {
	}

	public ChatMessageDTO(String message, String user) {
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
		return "ChatMessageDTO [message=" + message + ", user=" + user + "]";
	}

}

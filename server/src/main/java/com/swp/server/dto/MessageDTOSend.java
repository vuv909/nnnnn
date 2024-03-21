package com.swp.server.dto;

public class MessageDTOSend {

	private int roomId;
	private String message;
	private int accountId;

	public MessageDTOSend() {
		// TODO Auto-generated constructor stub
	}

	public MessageDTOSend(int roomId, String message, int accountId) {
		super();
		this.roomId = roomId;
		this.message = message;
		this.accountId = accountId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

}

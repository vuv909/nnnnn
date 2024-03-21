package com.swp.server.dto;

public class MessageReturn {
	private long messageId;
	private long chatId;
	private String sender;
	private String timestamp;
	private String content;

	// Constructors, getters, and setters

	public MessageReturn() {
	}

	public MessageReturn(long messageId, long chatId, String sender, String timestamp, String content) {
		this.messageId = messageId;
		this.chatId = chatId;
		this.sender = sender;
		this.timestamp = timestamp;
		this.content = content;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public long getChatId() {
		return chatId;
	}

	public void setChatId(long chatId) {
		this.chatId = chatId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}

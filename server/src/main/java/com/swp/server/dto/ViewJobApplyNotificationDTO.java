package com.swp.server.dto;

public class ViewJobApplyNotificationDTO {
	private String Email;
	private String Content;
	private String Created_At;
	private boolean isRead;

	public ViewJobApplyNotificationDTO() {
	}

	public ViewJobApplyNotificationDTO(String email, String content, String created_At, boolean isRead) {
		super();
		Email = email;
		Content = content;
		Created_At = created_At;
		this.isRead = isRead;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getCreated_At() {
		return Created_At;
	}

	public void setCreated_At(String created_At) {
		Created_At = created_At;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

}

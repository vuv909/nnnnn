package com.swp.server.dto;

import java.sql.Date;
import java.sql.Timestamp;

public class SendJobApplyNotificationDTO {
	private String Email;
	private String Content;
	private Timestamp Created_At;

	public SendJobApplyNotificationDTO() {
	}

	public SendJobApplyNotificationDTO(String email, String content, Timestamp created_At) {
		Email = email;
		Content = content;
		Created_At = created_At;
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

	public Timestamp getCreated_At() {
		return Created_At;
	}

	public void setCreated_At(Timestamp created_At) {
		Created_At = created_At;
	}

	@Override
	public String toString() {
		return "SendJobApplyNotificationDTO [Email=" + Email + ", Content=" + Content + ", Created_At=" + Created_At
				+ "]";
	}

}

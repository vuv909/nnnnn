package com.swp.server.dto;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class ReceiverOtpCode {

	private String email;

	public ReceiverOtpCode() {
		// TODO Auto-generated constructor stub
	}

	public ReceiverOtpCode(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

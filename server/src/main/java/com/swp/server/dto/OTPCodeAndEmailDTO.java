package com.swp.server.dto;

public class OTPCodeAndEmailDTO {
	private String otpCode;
	private String email;

	public OTPCodeAndEmailDTO() {
		// TODO Auto-generated constructor stub
	}

	public OTPCodeAndEmailDTO(String otpCode, String email) {
		super();
		this.otpCode = otpCode;
		this.email = email;
	}

	public String getOtpCode() {
		return otpCode;
	}

	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

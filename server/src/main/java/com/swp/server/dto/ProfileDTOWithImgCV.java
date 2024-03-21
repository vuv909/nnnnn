package com.swp.server.dto;

import org.springframework.web.multipart.MultipartFile;

public class ProfileDTOWithImgCV {
	private String email;
	private String firstName;
	private String lastName;
	private boolean gender;
	private String address;
	private String phoneNumber;
	private byte[] cv;
	private byte[] avatar;

	public ProfileDTOWithImgCV() {
		// TODO Auto-generated constructor stub
	}

	public ProfileDTOWithImgCV(String email, String firstName, String lastName, boolean gender, String address,
			String phoneNumber, byte[] cv, byte[] avatar) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.cv = cv;
		this.avatar = avatar;
	}

	public byte[] getCv() {
		return cv;
	}

	public void setCv(byte[] cv) {
		this.cv = cv;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}

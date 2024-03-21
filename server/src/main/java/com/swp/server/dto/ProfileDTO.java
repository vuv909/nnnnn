package com.swp.server.dto;

import com.swp.server.entities.Account;
import jakarta.persistence.Lob;
import org.springframework.web.multipart.MultipartFile;

public class ProfileDTO {
	private int id;
	private String email;
	private String firstName;
	private String lastName;
	private boolean gender;
	private String address;
	private String phoneNumber;
	private byte[] avatarImg;

	private MultipartFile CV;

	private MultipartFile avatar;

	public ProfileDTO() {
	}

	public ProfileDTO(String email, String firstName, String lastName, boolean gender, String address,
			String phoneNumber, MultipartFile CV, MultipartFile avatar) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.CV = CV;
		this.avatar = avatar;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public MultipartFile getCV() {
		return CV;
	}

	public void setCV(MultipartFile CV) {
		this.CV = CV;
	}

	public MultipartFile getAvatar() {
		return avatar;
	}

	public void setAvatar(MultipartFile avatar) {
		this.avatar = avatar;
	}

	public byte[] getAvatarImg() {
		return avatarImg;
	}

	public void setAvatarImg(byte[] avatarImg) {
		this.avatarImg = avatarImg;
	}

}

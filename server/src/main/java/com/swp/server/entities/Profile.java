package com.swp.server.entities;

import jakarta.persistence.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Entity
@Table(name = "Profile")
public class Profile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id", referencedColumnName = "id")
	private Account account;
	@Column(columnDefinition = "nvarchar(255)")
	private String firstName;
	@Column(columnDefinition = "nvarchar(255)")
	private String lastName;
	private boolean gender;
	@Column(columnDefinition = "nvarchar(255)")
	private String address;
	private String phoneNumber;
	@Lob
	private byte[] CV;
	@Lob
	private byte[] avatar;

	public Profile() {
	}

	public Profile(int id, Account account, String firstName, String lastName, boolean gender, String address,
			String phoneNumber, byte[] CV, byte[] avatar) {
		this.id = id;
		this.account = account;
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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
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

	public byte[] getCV() {
		return CV;
	}

	public String getStringCV() {
		Charset charset = StandardCharsets.UTF_8;
		return new String(CV, charset);
	}

	public void setCV(byte[] CV) {
		this.CV = CV;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}
}

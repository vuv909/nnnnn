package com.swp.server.entities;

import jakarta.persistence.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Date;

@Entity
@Table(name = "Job_Application")
public class Job_Application {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int Id;
	@ManyToOne
	@JoinColumn(name = "Account_Id", referencedColumnName = "Id")
	private Account account;
	@ManyToOne
	@JoinColumn(name = "Job_Id", referencedColumnName = "Id")
	private Job job;
	@Column(name = "Status")
	private String status;
	@Column(name = "Full_Name")
	private String Full_Name;
	@Column(name = "Email")
	private String Email;
	@Column(name = "Phone_Number")
	private String Phone_Number;
	@Lob
	@Column(name = "CV")
	private byte[] CV;
	@Column(name = "Cover_Letter")
	private String Cover_Letter;
	@Column(name = "HR_Email")
	private String Hr_Email;
	@Column(name = "Created_At")
	private Date Created_At;
	@Column(name = "Updated_At")
	private Date Updated_At;

	public Job_Application() {
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public String getHr_Email() {
		return Hr_Email;
	}

	public void setHr_Email(String hr_Email) {
		Hr_Email = hr_Email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStringCV() {
		Charset charset = StandardCharsets.UTF_8;
		return new String(CV, charset);
	}

	public String getFull_Name() {
		return Full_Name;
	}

	public void setFull_Name(String full_Name) {
		Full_Name = full_Name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPhone_Number() {
		return Phone_Number;
	}

	public void setPhone_Number(String phone_Number) {
		Phone_Number = phone_Number;
	}

	public byte[] getCV() {
		return CV;
	}

	public void setCV(byte[] CV) {
		this.CV = CV;
	}

	public String getCover_Letter() {
		return Cover_Letter;
	}

	public void setCover_Letter(String cover_Letter) {
		Cover_Letter = cover_Letter;
	}

	public Date getCreated_At() {
		return Created_At;
	}

	public void setCreated_At(Date created_At) {
		Created_At = created_At;
	}

	public Date getUpdated_At() {
		return Updated_At;
	}

	public void setUpdated_At(Date updated_At) {
		Updated_At = updated_At;
	}
}

package com.swp.server.dto;

import java.sql.Date;

import com.swp.server.entities.Account;
import com.swp.server.entities.Job;

public class ViewJobApplicationJob {
	private int Id;
	private int accountId;
	private Job job;
	private String status;
	private String full_Name;
	private String email;
	private String phone_Number;
	private byte[] CV;
	private String cover_Letter;
	private Date created_At;
	private Date updated_At;

	public ViewJobApplicationJob() {
		// TODO Auto-generated constructor stub
	}

	public ViewJobApplicationJob(int id, int accountId, Job job, String status, String full_Name, String email,
			String phone_Number, byte[] cV, String cover_Letter, Date created_At, Date updated_At) {
		super();
		Id = id;
		this.accountId = accountId;
		this.job = job;
		this.status = status;
		this.full_Name = full_Name;
		this.email = email;
		this.phone_Number = phone_Number;
		CV = cV;
		this.cover_Letter = cover_Letter;
		this.created_At = created_At;
		this.updated_At = updated_At;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFull_Name() {
		return full_Name;
	}

	public void setFull_Name(String full_Name) {
		this.full_Name = full_Name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone_Number() {
		return phone_Number;
	}

	public void setPhone_Number(String phone_Number) {
		this.phone_Number = phone_Number;
	}

	public byte[] getCV() {
		return CV;
	}

	public void setCV(byte[] CV) {
		this.CV = CV;
	}

	public String getCover_Letter() {
		return cover_Letter;
	}

	public void setCover_Letter(String cover_Letter) {
		this.cover_Letter = cover_Letter;
	}

	public Date getCreated_At() {
		return created_At;
	}

	public void setCreated_At(Date created_At) {
		this.created_At = created_At;
	}

	public Date getUpdated_At() {
		return updated_At;
	}

	public void setUpdated_At(Date updated_At) {
		this.updated_At = updated_At;
	}
}

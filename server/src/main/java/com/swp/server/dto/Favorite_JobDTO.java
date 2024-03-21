package com.swp.server.dto;

import com.swp.server.entities.Account;
import com.swp.server.entities.Job;

public class Favorite_JobDTO {

	private int id;
	private int jobId;
	private int accountId;
	private String nameJob;

	public Favorite_JobDTO() {
		// TODO Auto-generated constructor stub
	}

	public Favorite_JobDTO(int id, int jobId, int accountId, String nameJob) {
		super();
		this.id = id;
		this.jobId = jobId;
		this.accountId = accountId;
		this.nameJob = nameJob;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getNameJob() {
		return nameJob;
	}

	public void setNameJob(String nameJob) {
		this.nameJob = nameJob;
	}

}
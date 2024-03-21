package com.swp.server.dto;

import java.util.Date;

public class JobApplyDTO {
//    private int id;
	private String email;
	private int job_Id;
	private String cover_Letter;

	public JobApplyDTO() {
	}

	public JobApplyDTO(String email, int job_Id, String cover_Letter) {
		this.email = email;
		this.job_Id = job_Id;
		this.cover_Letter = cover_Letter;
	}

//    public JobApplyDTO(int id, String email, int job_Id, String cover_Letter) {
//        this.id = id;
//        this.email = email;
//        this.job_Id = job_Id;
//        this.cover_Letter = cover_Letter;
//    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getJob_Id() {
		return job_Id;
	}

	public void setJob_Id(int job_Id) {
		this.job_Id = job_Id;
	}

	public String getCover_Letter() {
		return cover_Letter;
	}

	public void setCover_Letter(String cover_Letter) {
		this.cover_Letter = cover_Letter;
	}
}

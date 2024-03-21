package com.swp.server.dto;

public class InterviewDTO {
	private String jobName;
	private String time;
	private String date;
	private String address;
	private String supporter;
	private String email;

	// Constructors
	public InterviewDTO() {
	}

	public InterviewDTO(String jobName, String time, String date, String address, String supporter) {
		this.jobName = jobName;
		this.time = time;
		this.date = date;
		this.address = address;
		this.supporter = supporter;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// Getters and setters
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSupporter() {
		return supporter;
	}

	public void setSupporter(String supporter) {
		this.supporter = supporter;
	}

	// toString method
	@Override
	public String toString() {
		return "Data{" + "jobName='" + jobName + '\'' + ", time='" + time + '\'' + ", date='" + date + '\''
				+ ", address='" + address + '\'' + ", supporter='" + supporter + '\'' + '}';
	}
}

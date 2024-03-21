package com.swp.server.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "Job_Apply_Notification")
public class JobApplyNotification {
	@jakarta.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int Id;
	@Column(columnDefinition = "nvarchar(255)")
	private String Content;
	@Column(name = "Email")
	private String email;
	@Column(name = "isRead")
	private boolean isRead;
	@Column(columnDefinition = "datetime")
	private Timestamp created_At;

	public JobApplyNotification() {
		// TODO Auto-generated constructor stub
	}

	public JobApplyNotification(int id, String content, String email, boolean isRead, Timestamp created_At) {
		super();
		Id = id;
		Content = content;
		this.email = email;
		this.isRead = isRead;
		this.created_At = created_At;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public Timestamp getCreated_At() {
		return created_At;
	}

	public void setCreated_At(Timestamp created_At) {
		this.created_At = created_At;
	}
}

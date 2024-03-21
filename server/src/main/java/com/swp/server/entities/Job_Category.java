package com.swp.server.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "Job_Category")
public class Job_Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int Id;
	@OneToMany(mappedBy = "job_category", cascade = CascadeType.ALL)
	private List<Job> jobs;
	@Column(name = "Name")
	private String Name;
	@Lob
	private byte[] Image;
	@Column(name = "isDeleted")
	private boolean isDeleted;
	@Column(name = "Created_At")
	private Date Created_At;
	@Column(name = "Updated_At")
	private Date Updated_At;

	public Job_Category() {
	}

	public Job_Category(int id, List<Job> jobs, String name, byte[] image, boolean isDeleted, Date created_At,
			Date updated_At) {
		super();
		Id = id;
		this.jobs = jobs;
		Name = name;
		Image = image;
		this.isDeleted = isDeleted;
		Created_At = created_At;
		Updated_At = updated_At;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public byte[] getImage() {
		return Image;
	}

	public void setImage(byte[] image) {
		Image = image;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
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

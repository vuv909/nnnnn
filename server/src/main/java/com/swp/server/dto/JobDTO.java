package com.swp.server.dto;

import java.sql.Date;

public class JobDTO {
	private int id;
	private int Category_Id;
	private String categoryName;
	private String name;
	private String Career_Level;
	private int Experience;
	private String Offer_Salary;
	private String Qualification;
	private String Job_Type;
	private int Branch_Id;
	private String Description;
	private Date Apply_Before;
	private String Address;
	private String status;
	private Date create_At;
	private Date update_At;
	private String branchName;
	private String hrEmail;
	private boolean isDelete;

	public String getHrEmail() {
		return hrEmail;
	}

	public void setHrEmail(String hrEmail) {
		this.hrEmail = hrEmail;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public JobDTO() {
	}

	public int getCategory_Id() {
		return Category_Id;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public void setCategory_Id(int category_Id) {
		Category_Id = category_Id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCareer_Level() {
		return Career_Level;
	}

	public void setCareer_Level(String career_Level) {
		Career_Level = career_Level;
	}

	public int getExperience() {
		return Experience;
	}

	public void setExperience(int experience) {
		Experience = experience;
	}

	public String getOffer_Salary() {
		return Offer_Salary;
	}

	public void setOffer_Salary(String offer_Salary) {
		Offer_Salary = offer_Salary;
	}

	public String getQualification() {
		return Qualification;
	}

	public void setQualification(String qualification) {
		Qualification = qualification;
	}

	public String getJob_Type() {
		return Job_Type;
	}

	public void setJob_Type(String job_Type) {
		Job_Type = job_Type;
	}

	public int getBranch_Id() {
		return Branch_Id;
	}

	public void setBranch_Id(int branch_Id) {
		Branch_Id = branch_Id;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Date getApply_Before() {
		return Apply_Before;
	}

	public void setApply_Before(Date apply_Before) {
		Apply_Before = apply_Before;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public JobDTO(int id, int category_Id, String categoryName, String name, String career_Level, int experience,
			String offer_Salary, String qualification, String job_Type, int branch_Id, String description,
			Date apply_Before, String address, String status, Date create_At, Date update_At) {
		super();
		this.id = id;
		Category_Id = category_Id;
		this.categoryName = categoryName;
		this.name = name;
		Career_Level = career_Level;
		Experience = experience;
		Offer_Salary = offer_Salary;
		Qualification = qualification;
		Job_Type = job_Type;
		Branch_Id = branch_Id;
		Description = description;
		Apply_Before = apply_Before;
		Address = address;
		this.status = status;
		this.create_At = create_At;
		this.update_At = update_At;
	}

	public Date getCreate_At() {
		return create_At;
	}

	public void setCreate_At(Date create_At) {
		this.create_At = create_At;
	}

	public Date getUpdate_At() {
		return update_At;
	}

	public void setUpdate_At(Date update_At) {
		this.update_At = update_At;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "JobDTO [id=" + id + ", Category_Id=" + Category_Id + ", categoryName=" + categoryName + ", name=" + name
				+ ", Career_Level=" + Career_Level + ", Experience=" + Experience + ", Offer_Salary=" + Offer_Salary
				+ ", Qualification=" + Qualification + ", Job_Type=" + Job_Type + ", Branch_Id=" + Branch_Id
				+ ", Description=" + Description + ", Apply_Before=" + Apply_Before + ", Address=" + Address
				+ ", status=" + status + ", create_At=" + create_At + ", update_At=" + update_At + "]";
	}

}

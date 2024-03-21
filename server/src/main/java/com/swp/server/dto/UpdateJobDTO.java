package com.swp.server.dto;

import java.util.Date;

public class UpdateJobDTO {
	  private String name;
	    private int Category_Id;
	    private String Career_Level;
	    private int Experience;
	    private String Offer_Salary;
	    private String Qualification;
	    private String Job_Type;
	    private int Branch_Id;
	    private String Description;
	    private Date Apply_Before;
	    private String Address;

	    // Getters and setters

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public int getCategory_Id() {
	        return Category_Id;
	    }

	    public void setCategory_Id(int Category_Id) {
	        this.Category_Id = Category_Id;
	    }

	    public String getCareer_Level() {
	        return Career_Level;
	    }

	    public void setCareer_Level(String Career_Level) {
	        this.Career_Level = Career_Level;
	    }

	    public int getExperience() {
	        return Experience;
	    }

	    public void setExperience(int Experience) {
	        this.Experience = Experience;
	    }

	    public String getOffer_Salary() {
	        return Offer_Salary;
	    }

	    public void setOffer_Salary(String Offer_Salary) {
	        this.Offer_Salary = Offer_Salary;
	    }

	    public String getQualification() {
	        return Qualification;
	    }

	    public void setQualification(String Qualification) {
	        this.Qualification = Qualification;
	    }

	    public String getJob_Type() {
	        return Job_Type;
	    }

	    public void setJob_Type(String Job_Type) {
	        this.Job_Type = Job_Type;
	    }

	    public int getBranch_Id() {
	        return Branch_Id;
	    }

	    public void setBranch_Id(int Branch_Id) {
	        this.Branch_Id = Branch_Id;
	    }

	    public String getDescription() {
	        return Description;
	    }

	    public void setDescription(String Description) {
	        this.Description = Description;
	    }

	    public Date getApply_Before() {
	        return Apply_Before;
	    }

	    public void setApply_Before(Date Apply_Before) {
	        this.Apply_Before = Apply_Before;
	    }

	    public String getAddress() {
	        return Address;
	    }

	    public void setAddress(String Address) {
	        this.Address = Address;
	    }
}

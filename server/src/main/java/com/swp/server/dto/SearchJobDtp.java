package com.swp.server.dto;

public class SearchJobDtp {

	private String text;
	private Integer category;
	private Integer branch;
	private String career_level;
	private Integer experience;
	private String salary;
	private String qualification;

	public SearchJobDtp() {
	}

	public SearchJobDtp(String text, Integer category, Integer branch, String career_level, Integer experience, String salary,
						String qualification) {
		super();
		this.text = text;
		this.category = category;
		this.branch = branch;
		this.career_level = career_level;
		this.experience = experience;
		this.salary = salary;
		this.qualification = qualification;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Integer getBranch() {
		return branch;
	}

	public void setBranch(Integer branch) {
		this.branch = branch;
	}

	public String getCareer_level() {
		return career_level;
	}

	public void setCareer_level(String career_level) {
		this.career_level = career_level;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	@Override
	public String toString() {
		return "SearchJobDtp [text=" + text + ", category=" + category + ", branch=" + branch + ", career_level="
				+ career_level + ", experience=" + experience + ", salary=" + salary + ", qualification="
				+ qualification + "]";
	}

}

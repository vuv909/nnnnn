package com.swp.server.repository;

import com.swp.server.entities.Job;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepo extends JpaRepository<Job, Integer> {
	@Query("SELECT j FROM Job j WHERE " + "(:text IS NULL OR j.name LIKE CONCAT('%', :text, '%')) "
			+ "AND (:category IS NULL OR j.job_category.Id = :category) "
			+ "AND (:branch IS NULL OR j.branch.id = :branch) "
			+ "AND (:careerLevel IS NULL OR j.Career_Level = :careerLevel) "
			+ "AND (:experience IS NULL OR j.Experience = :experience) "
			+ "AND (:salary IS NULL OR j.Offer_Salary = :salary) "
			+ "AND (:qualification IS NULL OR j.Qualification = :qualification)")
	List<Job> findByCriteria(@Param("text") String text, @Param("category") Integer category,
			@Param("branch") Integer branch, @Param("careerLevel") String careerLevel,
			@Param("experience") Integer experience, @Param("salary") String salary,
			@Param("qualification") String qualification);

	List<Job> findByHrEmail(String email);

	@Query("SELECT ja FROM Job ja WHERE ja.job_category.Id = ?1")
	List<Job> findAllByCategoryId(int categoryId);

}

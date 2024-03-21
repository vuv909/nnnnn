package com.swp.server.services.job;

import com.swp.server.dto.*;
import com.swp.server.entities.Job;

import java.util.List;

import org.hibernate.sql.Update;
import org.springframework.http.ResponseEntity;

public interface JobService {
	public ResponseEntity<?> createJob(JobDTO jobDTO);

	public ResponseEntity<?> viewJob(Integer page);

	public ResponseEntity<?> updateJob(JobDTO jobDTO);

	public ResponseEntity<?> getJobInforById(int Id);

	public ResponseEntity<?> createJobCategory(JobCategoryDTO jobDTO);

	ResponseEntity<?> updateJobCategory(UpdateJobCategoryDTO jobDTO);

	public ResponseEntity<?> deleteJobCategory(int jobId);

	public ResponseEntity<?> deleteJob(int jobId);

	ResponseEntity<?> viewJobCategory(Integer page);

	public ResponseEntity<?> getAllBranch();

	public ResponseEntity<?> searchJob(SearchJobDtp searchJobDtp, int page);

	public ResponseEntity<?> viewByHrEmail(String email, Integer page);

	public ResponseEntity<?> viewJobByCategoryId(int categoryId);

	public ResponseEntity<?> countJobByYear(YearDTO yearDTO);

	public ResponseEntity<?> listWeekByYear(YearDTO yearDTO);

	public ResponseEntity<?> countJobByWeek(DateDTO dateDTO);
}

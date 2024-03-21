package com.swp.server.services.job_application;

import com.swp.server.dto.InterviewDTO;
import com.swp.server.dto.JobApplyDTO;
import com.swp.server.dto.UpdateJobApplyDTO;
import com.swp.server.dto.UpdateJobCategoryDTO;
import org.springframework.http.ResponseEntity;

public interface JobApplicationService {
	public ResponseEntity<?> applyJob(JobApplyDTO jobApplyDTO);

	public ResponseEntity<?> updateJobApplication(UpdateJobApplyDTO updateJobApplyDTO);

	public ResponseEntity<?> viewJobApplicationByAccountId(String email, int page);

	public ResponseEntity<?> viewJobApplication(Integer page);

	public ResponseEntity<?> viewJobApplicationByHrEmail(String email, String status, Integer page);

	public ResponseEntity<?> sendMailInterview(InterviewDTO jobApplyDTO);
}

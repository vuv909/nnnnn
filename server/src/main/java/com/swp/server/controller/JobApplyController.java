package com.swp.server.controller;

import com.swp.server.dto.InterviewDTO;
import com.swp.server.dto.JobApplyDTO;
import com.swp.server.dto.UpdateJobApplyDTO;
import com.swp.server.entities.Account;
import com.swp.server.entities.Job_Application;
import com.swp.server.entities.Profile;
import com.swp.server.repository.AccountRepo;
import com.swp.server.repository.JobApplyRepo;
import com.swp.server.repository.ProfileRepo;
import com.swp.server.services.job_application.JobApplicationService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/jobApply")
public class JobApplyController {
	@Autowired
	private JobApplyRepo jobApplyRepo;
	@Autowired
	private JobApplicationService jobApplicationService;

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private ProfileRepo profileRepo;

	@PostMapping("/createJobApply")
	public ResponseEntity<?> applyJob(@ModelAttribute JobApplyDTO jobApplyDTO) {
		return jobApplicationService.applyJob(jobApplyDTO);
	}

	@PostMapping("/sendMailInterview")
	public ResponseEntity<?> sendMailInterview(@RequestBody InterviewDTO jobApplyDTO) {
		return jobApplicationService.sendMailInterview(jobApplyDTO);
	}

	@PutMapping("/updateJobApply")
	public ResponseEntity<?> updateJobApplication(@ModelAttribute UpdateJobApplyDTO updateJobApplyDTO) {
		return jobApplicationService.updateJobApplication(updateJobApplyDTO);
	}

	@GetMapping("/view/{email}")
	public ResponseEntity<?> viewJobApplicationByAccountId(@PathVariable String email,
			@RequestParam(value = "page", required = true) Integer page) {
		return jobApplicationService.viewJobApplicationByAccountId(email, page);
	}

	@GetMapping("/view/hremail/{email}")
	public ResponseEntity<?> viewJobApplicationByHrEmail(@PathVariable String email,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "status", required = false) String status) {
		return jobApplicationService.viewJobApplicationByHrEmail(email, status, page);
	}

	@GetMapping("/view")
	public ResponseEntity<?> viewJobApplication(@RequestParam(value = "page", required = false) Integer page) {
		return jobApplicationService.viewJobApplication(page);
	}

	// dowload cv
	@GetMapping("/downloadFile/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String id, HttpServletRequest request) {
		try {

			Optional<Job_Application> optional = jobApplyRepo.findFirstById(Integer.parseInt(id));

			if (!optional.isPresent()) {
				// Handle case when profile is not found
				return ResponseEntity.notFound().build();
			}

			// Convert varbinary(MAX) to byte array
			byte[] fileBytes = optional.get().getCV();

			ByteArrayResource resource = new ByteArrayResource(fileBytes);

			// Set the content type specifically for PDF files
			String contentType = "application/pdf";

			String cleanedFileName = cleanHeaderField(optional.get().getStringCV());

			HttpHeaders headers = new HttpHeaders();

			headers.add("Content-Disposition", "attachment; filename=CV.pdf");
			headers.add("Access-Control-Expose-Headers", "*");
			headers.add("Content-type", "application/pdf");

			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	private String cleanHeaderField(String fieldValue) {
		// Remove CR/LF characters from the header field value
		return fieldValue.replace("\r", "").replace("\n", "");
	}

}

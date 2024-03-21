package com.swp.server.controller;

import com.swp.server.dto.*;
import com.swp.server.repository.JobApplyRepo;
import com.swp.server.repository.JobCategoryRepo;
import com.swp.server.repository.JobRepo;
import com.swp.server.services.job.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/job")
public class JobController {

	@Autowired
	private JobApplyRepo jobApplyRepo;

	@Autowired
	private JobCategoryRepo jobCategoryRepo;

	@Autowired
	private JobRepo jobRepo;

	@Autowired
	private JobService jobService;

	@PostMapping("/searchJob")
	public ResponseEntity<?> searchJob(@ModelAttribute SearchJobDtp searchJobDtp,
			@RequestParam(value = "page", required = true) Integer page) {
		System.out.print(searchJobDtp.toString());
		return jobService.searchJob(searchJobDtp, page);
	}

	@GetMapping("/viewJob")
	public ResponseEntity<?> viewJob(@RequestParam(value = "page", required = false) Integer page) {
		return jobService.viewJob(page);
	}

	@GetMapping("/viewJobByCate/{categoryId}")
	public ResponseEntity<?> viewJobByCategoryId(@PathVariable int categoryId) {
		return jobService.viewJobByCategoryId(categoryId);
	}

	@GetMapping("/viewJobCategory")
	public ResponseEntity<?> viewJobCategory(@RequestParam(value = "page", required = false) Integer page) {
		return jobService.viewJobCategory(page);
	}

	@GetMapping("/viewBranch")
	public ResponseEntity<?> viewbranch() {
		return jobService.getAllBranch();
	}

	@PostMapping("/createJob")
	public ResponseEntity<?> createJob(@ModelAttribute JobDTO jobDTO) {
		return jobService.createJob(jobDTO);
	}

	@DeleteMapping("/deleteJob/{jobId}")
	public ResponseEntity<?> deleteJob(@PathVariable int jobId) {
		return jobService.deleteJob(jobId);
	}

	@GetMapping("/view/{jobId}")
	public ResponseEntity<?> viewJobInfor(@PathVariable int jobId) {
		return jobService.getJobInforById(jobId);
	}

	@GetMapping("/view/hremail/{email}")
	public ResponseEntity<?> viewByHrEmail(@PathVariable String email,
			@RequestParam(value = "page", required = false) Integer page) {
		return jobService.viewByHrEmail(email, page);
	}

	@PutMapping("/edit")
	public ResponseEntity<?> updateJob(@ModelAttribute JobDTO jobDTO) {
		return jobService.updateJob(jobDTO);
	}

	@GetMapping("/branch/getAll")
	public ResponseEntity<?> getAllBranch() {
		return jobService.getAllBranch();
	}

	@PostMapping("/job_category")
	public ResponseEntity<?> createJobCategory(@ModelAttribute JobCategoryDTO jobDTO) {
		return jobService.createJobCategory(jobDTO);
	}

	@PostMapping("/update/job_category")
	public ResponseEntity<?> updateJob(@ModelAttribute UpdateJobCategoryDTO jobDTO) {
		return jobService.updateJobCategory(jobDTO);
	}

	@DeleteMapping("/delete/job_category/{jobId}")
	public ResponseEntity<?> deleteJobCategory(@PathVariable int jobId) {
		return jobService.deleteJobCategory(jobId);
	}

	@PostMapping("/countJobByYear")
	public ResponseEntity<?> countJobByYear(@RequestBody YearDTO yearDTO) {
		return jobService.countJobByYear(yearDTO);
	}

	@GetMapping("/listWeekByYear")
	public ResponseEntity<?> listWeekByYear(@RequestBody YearDTO yearDTO) {
		return jobService.listWeekByYear(yearDTO);
	}

	@PostMapping("/countJobByWeek")
	public ResponseEntity<?> countJobByWeek(@RequestBody DateDTO dateDTO) {
		return jobService.countJobByWeek(dateDTO);
	}
}

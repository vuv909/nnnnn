package com.swp.server.services.job;

import com.swp.server.dto.*;
import com.swp.server.entities.Branch;
import com.swp.server.entities.Job;
import com.swp.server.entities.Job_Category;
import com.swp.server.repository.BranchRepo;
import com.swp.server.repository.JobApplyRepo;
import com.swp.server.repository.JobCategoryRepo;
import com.swp.server.repository.JobRepo;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
@Slf4j
public class JobServiceImpl implements JobService {
	@Autowired
	private JobApplyRepo jobApplyRepo;

	@Autowired
	private JobCategoryRepo jobCategoryRepo;

	@Autowired
	private BranchRepo branchRepo;

	@Autowired
	private JobRepo jobRepo;

	@Override
	public ResponseEntity<?> searchJob(SearchJobDtp searchJobDtp, int page) {
		try {
			// Extract search criteria from SearchJobDtp
			System.err.println(searchJobDtp.toString());
			String text = searchJobDtp.getText();
			Integer category = searchJobDtp.getCategory();
			Integer branch = searchJobDtp.getBranch();
			String careerLevel = searchJobDtp.getCareer_level();
			Integer experience = searchJobDtp.getExperience();
			String salary = searchJobDtp.getSalary();
			String qualification = searchJobDtp.getQualification();
			System.err.println("Branch: " + branch);
			// Call the repository method with search criteria
			List<Job> jobList = new ArrayList<Job>();
			List<Job> abc = jobRepo.findByCriteria(text, category, branch, careerLevel, experience, salary,
					qualification);

			// Check if the job list is empty
			if (abc.isEmpty()) {
				// If the list is empty, return a ResponseEntity with an error message
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No jobs found matching the search criteria.");
			} else {
				for (Job fJob : abc) {
					if (!fJob.isDeleted() && fJob.getApply_Before().after(new Date())) {

						Job job = new Job();
						job.setAddress(fJob.getAddress());
						job.setName(fJob.getName());
						job.setDescription(fJob.getDescription());
						job.setId(fJob.getId());
						job.setAddress(fJob.getAddress());
						job.setJob_Type(fJob.getJob_Type());
						job.setHrEmail(fJob.getHrEmail());
						job.setApply_Before(fJob.getApply_Before());
						jobList.add(job);
					}
				}
				int totalJobs = jobList.size();
				int limit = 5; // Number of jobs per page
				int totalPages = (int) Math.ceil((double) totalJobs / limit);
				int startIndex = (page - 1) * limit;
				int endIndex = Math.min(startIndex + limit, totalJobs);

				List<Job> paginatedJobs = jobList.subList(startIndex, endIndex);

				// Prepare DTO for response
				ResultPaginationDTO<Job> dto = new ResultPaginationDTO<>();
				dto.setPage(page);
				dto.setListResults(paginatedJobs);
				dto.setTotalPage(totalPages);

				return ResponseEntity.ok(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No jobs found matching the search criteria.");
		}
	}

	@Override
	public ResponseEntity<?> viewJobByCategoryId(int categoryId) {
		List<JobDTO> jobDTOS = new ArrayList<>();
		List<Job> jobOptional = jobRepo.findAllByCategoryId(categoryId);
		if (jobOptional.isEmpty()) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "Job is empty");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
		for (Job job : jobOptional) {
			if (job.isDeleted() == false) {
				JobDTO jobDTO = new JobDTO();
				jobDTO.setId(job.getId());
				jobDTO.setName(job.getName());
				jobDTO.setCategoryName(job.getJob_category().getName());
				jobDTO.setCareer_Level(job.getCareer_Level());
				jobDTO.setExperience(job.getExperience());
				jobDTO.setOffer_Salary(job.getOffer_Salary());
				jobDTO.setQualification(job.getQualification());
				jobDTO.setJob_Type(job.getJob_Type());
				jobDTO.setDescription(job.getDescription());
				jobDTO.setApply_Before(job.getApply_Before());
				jobDTO.setHrEmail(job.getHrEmail());
				jobDTO.setAddress(job.getAddress());
				jobDTO.setUpdate_At(job.getUpdated_At());
				jobDTO.setCreate_At(job.getCreated_At());

				LocalDate applyBeforeDate = job.getApply_Before().toLocalDate();

				// Check if the job is expired or valid based on applyBefore date
				if (LocalDate.now().isAfter(applyBeforeDate)) {
					jobDTO.setStatus("expired");
				} else {
					jobDTO.setStatus("valid");
				}

				jobDTOS.add(jobDTO);
			}
		}
		return ResponseEntity.ok(jobDTOS);
	}

	@Override
	public ResponseEntity<?> createJobCategory(JobCategoryDTO jobDTO) {
		try {

			if (jobDTO.getImage() == null || jobDTO.getName() == null || jobDTO.getName().trim() == "") {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Không được để trống trường nào trong job category !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			} else {
				Date date = new Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				Job_Category job_Category = new Job_Category();
				job_Category.setImage(jobDTO.getImage().getBytes());
				job_Category.setName(jobDTO.getName());
				job_Category.setDeleted(false);
				job_Category.setCreated_At(sqlDate);
				jobCategoryRepo.save(job_Category);
				Map<String, Object> error = new HashMap<>();
				error.put("success", "Thêm thành công !!!");
				return new ResponseEntity<>(error, HttpStatus.OK);
			}

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> viewByHrEmail(String email, Integer page) {
		List<JobDTO> jobDTOS = new ArrayList<>();
		List<Job> jobOptional = jobRepo.findByHrEmail(email);
		if (jobOptional.isEmpty()) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "Job is empty");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
		for (Job job : jobOptional) {
			if (job.isDeleted() == false) {
				JobDTO jobDTO = new JobDTO();
				jobDTO.setId(job.getId());
				jobDTO.setName(job.getName());
				jobDTO.setCategoryName(job.getJob_category().getName());
				jobDTO.setCareer_Level(job.getCareer_Level());
				jobDTO.setExperience(job.getExperience());
				jobDTO.setOffer_Salary(job.getOffer_Salary());
				jobDTO.setQualification(job.getQualification());
				jobDTO.setJob_Type(job.getJob_Type());
				jobDTO.setDescription(job.getDescription());
				jobDTO.setApply_Before(job.getApply_Before());
				jobDTO.setHrEmail(job.getHrEmail());
				jobDTO.setAddress(job.getAddress());
				jobDTO.setUpdate_At(job.getUpdated_At());
				jobDTO.setCreate_At(job.getCreated_At());

				LocalDate applyBeforeDate = job.getApply_Before().toLocalDate();

				// Check if the job is expired or valid based on applyBefore date
				if (LocalDate.now().isAfter(applyBeforeDate)) {
					jobDTO.setStatus("expired");
				} else {
					jobDTO.setStatus("valid");
				}

				jobDTOS.add(jobDTO);
			}
		}
		if (page == null) {
			return ResponseEntity.ok(jobDTOS);
		}
		int totalJobs = jobDTOS.size();
		int limit = 5; // Number of jobs per page
		int totalPages = (int) Math.ceil((double) totalJobs / limit);
		int startIndex = (page - 1) * limit;
		int endIndex = Math.min(startIndex + limit, totalJobs);

		List<JobDTO> paginatedJobs = jobDTOS.subList(startIndex, endIndex);

		// Prepare DTO for response
		ResultPaginationDTO<JobDTO> dto = new ResultPaginationDTO<>();
		dto.setPage(page);
		dto.setListResults(paginatedJobs);
		dto.setTotalPage(totalPages);

		return ResponseEntity.ok(dto);
	}

	@Override
	public ResponseEntity<?> getAllBranch() {
		List<BranchDTO> branchDTO = new ArrayList<>();

		List<Branch> jobOptional = branchRepo.findAll().stream().toList();
		if (jobOptional.isEmpty()) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "Branch is empty");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
		for (Branch job : jobOptional) {
			BranchDTO branchDTOElement = new BranchDTO();
			branchDTOElement.setId(job.getId());
			branchDTOElement.setName(job.getName());
			branchDTOElement.setAddress(job.getAddress());
			branchDTOElement.setImg(job.getImg());
			branchDTO.add(branchDTOElement);

		}
		return ResponseEntity.ok(branchDTO);
	}

	@Override
	public ResponseEntity<?> updateJobCategory(UpdateJobCategoryDTO jobDTO) {
		try {

			if (jobDTO.getName() == null || jobDTO.getName().trim() == "") {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Vui lòng không được bỏ trống trường tên !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			} else {
				Optional<Job_Category> job_Category = jobCategoryRepo.findById(jobDTO.getId());
				if (job_Category.isPresent()) {
					if (jobDTO.getImage() == null) {
						Date date = new Date();
						java.sql.Date sqlDate = new java.sql.Date(date.getTime());
						job_Category.get().setName(jobDTO.getName());
						job_Category.get().setUpdated_At(sqlDate);
						jobCategoryRepo.save(job_Category.get());
						Map<String, Object> success = new HashMap<>();
						success.put("success", "Cập nhập thành công !!!");
						return new ResponseEntity<>(success, HttpStatus.OK);
					}
					Date date = new Date();
					java.sql.Date sqlDate = new java.sql.Date(date.getTime());
					job_Category.get().setImage(jobDTO.getImage().getBytes());
					job_Category.get().setName(jobDTO.getName());
					job_Category.get().setUpdated_At(sqlDate);
					jobCategoryRepo.save(job_Category.get());
					Map<String, Object> success = new HashMap<>();
					success.put("success", "Thêm thành công !!!");
					return new ResponseEntity<>(success, HttpStatus.OK);
				} else {
					Map<String, Object> error = new HashMap<>();
					error.put("error", "Not exist !!!");
					return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
				}

			}

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> deleteJobCategory(int jobId) {
		try {
			System.out.print("id" + jobId);
			Optional<Job_Category> job_Category = jobCategoryRepo.findById(jobId);
			if (job_Category.isPresent()) {
				job_Category.get().setDeleted(true);
				jobCategoryRepo.save(job_Category.get());
				Map<String, Object> success = new HashMap<>();
				success.put("success", "Xóa thành công !!!");
				return new ResponseEntity<>(success, HttpStatus.OK);
			} else {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Not exist !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> createJob(JobDTO jobDTO) {
		System.out.print(jobDTO.toString());
		try {
			Job job = new Job();
			Optional<Branch> optionalBranch = branchRepo.findById(jobDTO.getBranch_Id());
			if (optionalBranch.isPresent() == false) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Not found branch");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			Optional<Job_Category> jobCategoryOptional = jobCategoryRepo.findById(jobDTO.getCategory_Id());
			if (jobCategoryOptional.isEmpty()) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Job category not found");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			} else {
				job.setJob_category(jobCategoryOptional.get());
			}
			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			job.setJobApplications(null);
			if (jobDTO.getCareer_Level().equals("Manager") || jobDTO.getCareer_Level().equals("Fresher")
					|| jobDTO.getCareer_Level().equals("Junior") || jobDTO.getCareer_Level().equals("Senior")) {
				job.setCareer_Level(jobDTO.getCareer_Level());
			} else {
				job.setCareer_Level("Others");
			}
			if (jobDTO.getExperience() >= 0) {
				job.setExperience(jobDTO.getExperience());
			} else {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Please input experience >= 0");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			if (jobDTO.getOffer_Salary().equals("0-$1000") || jobDTO.getOffer_Salary().equals("$1000-$2000")
					|| jobDTO.getOffer_Salary().equals("$2000-$3000") || jobDTO.getOffer_Salary().equals("$3000-$5000")
					|| jobDTO.getQualification().equals("$5000++")) {
				job.setOffer_Salary(jobDTO.getOffer_Salary());
			} else {
				job.setOffer_Salary("Negotiable");
			}
			if (jobDTO.getQualification().equals("Certificate") || jobDTO.getQualification().equals("Diploma")
					|| jobDTO.getQualification().equals("Associate Degree")
					|| jobDTO.getQualification().equals("Bachelor Degree")
					|| jobDTO.getQualification().equals("Master’s Degree")
					|| jobDTO.getQualification().equals("Doctorate Degree")) {
				job.setQualification(jobDTO.getQualification());
			} else {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Wrong input");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			job.setName(jobDTO.getName());
			job.setJob_Type(jobDTO.getJob_Type());
			job.setDescription(jobDTO.getDescription());
			job.setApply_Before(jobDTO.getApply_Before());
			job.setAddress(jobDTO.getAddress());
			job.setCreated_At(sqlDate);
			job.setUpdated_At(null);
			job.setBranch(optionalBranch.get());
			job.setDeleted(false);
			job.setHrEmail(jobDTO.getHrEmail());
			jobRepo.save(job);
			Map<String, String> success = new HashMap<String, String>();
			success.put("success", "Job created successfully !!!");
			return new ResponseEntity<>(success, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> viewJob(Integer page) {
		List<JobDTO> jobDTOS = new ArrayList<>();
		List<Job> jobOptional = jobRepo.findAll().stream().toList();
		if (jobOptional.isEmpty()) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "Job is empty");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
		for (Job job : jobOptional) {
			if (job.isDeleted() == false) {
				JobDTO jobDTO = new JobDTO();
				jobDTO.setId(job.getId());
				jobDTO.setName(job.getName());
				jobDTO.setCategoryName(job.getJob_category().getName());
				jobDTO.setCareer_Level(job.getCareer_Level());
				jobDTO.setExperience(job.getExperience());
				jobDTO.setOffer_Salary(job.getOffer_Salary());
				jobDTO.setQualification(job.getQualification());
				jobDTO.setJob_Type(job.getJob_Type());
				jobDTO.setDescription(job.getDescription());
				jobDTO.setApply_Before(job.getApply_Before());
				jobDTO.setHrEmail(job.getHrEmail());
				jobDTO.setAddress(job.getAddress());
				jobDTO.setUpdate_At(job.getUpdated_At());
				jobDTO.setCreate_At(job.getCreated_At());

				LocalDate applyBeforeDate = job.getApply_Before().toLocalDate();

				// Check if the job is expired or valid based on applyBefore date
				if (LocalDate.now().isAfter(applyBeforeDate)) {
					jobDTO.setStatus("expired");
				} else {
					jobDTO.setStatus("valid");
				}

				jobDTOS.add(jobDTO);
			}
		}
		if (page == null) {
			return ResponseEntity.ok(jobDTOS);
		}
		int totalJobs = jobDTOS.size();
		int limit = 5; // Number of jobs per page
		int totalPages = (int) Math.ceil((double) totalJobs / limit);
		int startIndex = (page - 1) * limit;
		int endIndex = Math.min(startIndex + limit, totalJobs);

		List<JobDTO> paginatedJobs = jobDTOS.subList(startIndex, endIndex);

		// Prepare DTO for response
		ResultPaginationDTO<JobDTO> dto = new ResultPaginationDTO<>();
		dto.setPage(page);
		dto.setListResults(paginatedJobs);
		dto.setTotalPage(totalPages);

		return ResponseEntity.ok(dto);
	}

	@Override
	public ResponseEntity<?> viewJobCategory(Integer page) {
		List<JobCateDTO> jobCateDTOList = new ArrayList<>();
		List<Job_Category> jobOptional = jobCategoryRepo.findAll();
		if (jobOptional.isEmpty()) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "Job category is empty");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
		for (Job_Category jobElement : jobOptional) {
			if (!jobElement.isDeleted()) {
				JobCateDTO job = new JobCateDTO(); // Create new instance inside the loop
				job.setId(jobElement.getId());
				job.setName(jobElement.getName());
				job.setImage(jobElement.getImage());
				job.setCreate_At(jobElement.getCreated_At());
				job.setUpdate_At(jobElement.getUpdated_At());
				jobCateDTOList.add(job);
			}
		}
		if (page == null) {
			return ResponseEntity.ok(jobCateDTOList);
		}
		int totalJobs = jobCateDTOList.size();
		int limit = 5; // Number of jobs per page
		int totalPages = (int) Math.ceil((double) totalJobs / limit);
		int startIndex = (page - 1) * limit;
		int endIndex = Math.min(startIndex + limit, totalJobs);

		List<JobCateDTO> paginatedJobs = jobCateDTOList.subList(startIndex, endIndex);

		// Prepare DTO for response
		ResultPaginationDTO<JobCateDTO> dto = new ResultPaginationDTO<>();
		dto.setPage(page);
		dto.setListResults(paginatedJobs);
		dto.setTotalPage(totalPages);

		return ResponseEntity.ok(dto);

	}

	@Override
	public ResponseEntity<?> updateJob(JobDTO jobDTO) {
		try {
			Job job = new Job();
			Optional<Job> jobOptional = jobRepo.findById(jobDTO.getId());
			if (jobOptional.isEmpty()) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Job not found");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			} else {
				job = jobOptional.get();
			}

			Optional<Job_Category> jobCategoryOptional = jobCategoryRepo.findById(jobDTO.getCategory_Id());
			System.out.print(jobDTO.toString());
			if (jobCategoryOptional.isPresent()) {
				// Job category exists and is not null
				job.setJob_category(jobCategoryOptional.get());
			} else {
				// Job category not found or is null
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Job category not found");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}

			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			job.setJobApplications(null);
			if (jobDTO.getCareer_Level().equals("Manager") || jobDTO.getCareer_Level().equals("Fresher")
					|| jobDTO.getCareer_Level().equals("Junior") || jobDTO.getCareer_Level().equals("Senior")) {
				job.setCareer_Level(jobDTO.getCareer_Level());
			} else {
				job.setCareer_Level("Others");
			}
			if (jobDTO.getExperience() >= 0) {
				job.setExperience(jobDTO.getExperience());
			} else {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Please input experience >= 0");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			if (jobDTO.getOffer_Salary().equals("0-$1000") || jobDTO.getOffer_Salary().equals("$1000-$2000")
					|| jobDTO.getOffer_Salary().equals("$2000-$3000") || jobDTO.getOffer_Salary().equals("$3000-$5000")
					|| jobDTO.getQualification().equals("$5000++")) {
				job.setOffer_Salary(jobDTO.getOffer_Salary());
			} else {
				job.setOffer_Salary("Negotiable");
			}
			Optional<Branch> optionalBranch = branchRepo.findById(jobDTO.getBranch_Id());
			if (optionalBranch.isPresent() == false) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Not found branch");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			} else {
				job.setBranch(optionalBranch.get());
			}
			if (jobDTO.getQualification().equals("Certificate") || jobDTO.getQualification().equals("Diploma")
					|| jobDTO.getQualification().equals("Associate Degree")
					|| jobDTO.getQualification().equals("Bachelor Degree")
					|| jobDTO.getQualification().equals("Master’s Degree")
					|| jobDTO.getQualification().equals("Doctorate Degree")) {
				job.setQualification(jobDTO.getQualification());
			} else {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Wrong input");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			job.setName(jobDTO.getName());
			job.setJob_Type(jobDTO.getJob_Type());
			job.setDescription(jobDTO.getDescription());
			job.setApply_Before(jobDTO.getApply_Before());
			job.setAddress(jobDTO.getAddress());
			job.setUpdated_At(sqlDate);
			jobRepo.save(job);
			Map<String, String> success = new HashMap<String, String>();
			success.put("success", "Job updated successfully !!!");
			return new ResponseEntity<>(success, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> getJobInforById(int Id) {
		Job job = new Job();
		JobDTO jobDTO = new JobDTO();
		Optional<Job> jobOptional = jobRepo.findById(Id);
		if (jobOptional.isEmpty()) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "Job not found");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		} else {

			job = jobOptional.get();
			Optional<Branch> optionalBranch = branchRepo.findById(job.getBranch().getId());
			if (optionalBranch.isEmpty()) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Error !!!");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}
			jobDTO.setId(job.getId());
			jobDTO.setName(job.getName());
			jobDTO.setBranch_Id(job.getBranch().getId());
			jobDTO.setBranchName(optionalBranch.get().getName());
			jobDTO.setCategory_Id(job.getJob_category().getId());
			jobDTO.setCareer_Level(job.getCareer_Level());
			jobDTO.setExperience(job.getExperience());
			jobDTO.setOffer_Salary(job.getOffer_Salary());
			jobDTO.setQualification(job.getQualification());
			jobDTO.setJob_Type(job.getJob_Type());
			jobDTO.setDescription(job.getDescription());
			jobDTO.setApply_Before(job.getApply_Before());
			jobDTO.setAddress(job.getAddress());
			jobDTO.setJob_Type(job.getJob_Type());
			jobDTO.setCreate_At(job.getCreated_At());
			jobDTO.setUpdate_At(job.getUpdated_At());
			jobDTO.setHrEmail(job.getHrEmail());
			jobDTO.setDelete(job.isDeleted());
			return ResponseEntity.ok(jobDTO);
		}
	}

	@Override
	public ResponseEntity<?> deleteJob(int jobId) {
		try {

			Optional<Job> job = jobRepo.findById(jobId);
			if (job.isPresent()) {
				job.get().setDeleted(true);
				jobRepo.save(job.get());
				Map<String, Object> success = new HashMap<>();
				success.put("success", "Xóa thành công !!!");
				return new ResponseEntity<>(success, HttpStatus.OK);
			} else {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Not exist !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> countJobByYear(YearDTO yearDTO) {
		try {
			Map<String, Integer> jobCountMap = new HashMap<>();
			ArrayList<Integer> list = new ArrayList<>();
			int yearToInt = yearDTO.getYear();
			if (yearToInt <= 0) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "YEAR NOT VALID !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, yearToInt);
			int numberOfMonths = calendar.getActualMaximum(Calendar.MONTH) + 1;
			for (int i = 1; i <= numberOfMonths; i++) {
				List<Job> jobs = jobRepo.findAll().stream().toList();
				int numberOfJobs = 0;
				for (Job job : jobs) {
					calendar.setTime(job.getCreated_At());
					if (calendar.get(Calendar.MONTH) + 1 == i && calendar.get(Calendar.YEAR) == yearToInt) {
						numberOfJobs += 1;
					}
				}
				// switch (i) {
				// case 1:
				list.add(numberOfJobs);
				// jobCountMap.put("January", numberOfJobs);
				// case 2:

				// jobCountMap.put("February", numberOfJobs);
				// case 3:

				// jobCountMap.put("March", numberOfJobs);
				// case 4:

				// jobCountMap.put("April", numberOfJobs);
				// case 5:

				// jobCountMap.put("May", numberOfJobs);
				// case 6:

				// jobCountMap.put("June", numberOfJobs);
				// case 7:

				// jobCountMap.put("July", numberOfJobs);
				// case 8:

				// jobCountMap.put("August", numberOfJobs);
				// case 9:

				// jobCountMap.put("September", numberOfJobs);
				// case 10:

				// jobCountMap.put("October", numberOfJobs);
				// case 11:

				// jobCountMap.put("November", numberOfJobs);
				// case 12:

				// jobCountMap.put("December", numberOfJobs);
				// }
			}
			return ResponseEntity.ok(list);
		} catch (NumberFormatException e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "YEAR NOT VALID !!!");
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> listWeekByYear(YearDTO yearDTO) {
		int yearToInt = yearDTO.getYear();

		WeekFields weekFields = WeekFields.of(Locale.getDefault());

		LocalDate startDate = LocalDate.of(yearToInt, 1, 1);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		Map<String, String> weekMap = new HashMap<>();

		while (startDate.getYear() == yearToInt) {

			LocalDate weekStart = startDate;

			LocalDate weekEnd = startDate.plusDays(6);

			weekMap.put("Week " + startDate.get(weekFields.weekOfWeekBasedYear()),
					weekStart.format(formatter) + " To " + weekEnd.format(formatter));

			startDate = startDate.plusWeeks(1);
		}

		return ResponseEntity.ok(weekMap);
	}

	private static final SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

	@Override
	public ResponseEntity<?> countJobByWeek(DateDTO dateDTO) {
		Map<String, Integer> jobCountMap = new HashMap<>();
		ArrayList<Integer> list = new ArrayList<>();
		Calendar calendarDTO = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		calendarDTO.setTime(dateDTO.getStartDate());

		while (!calendarDTO.getTime().after(dateDTO.getEndDate())) {
			List<Job> jobs = jobRepo.findAll().stream().toList();
			int numberOfJobs = 0;
			for (Job job : jobs) {
				calendar.setTime(job.getCreated_At());
				if (isSameDay(calendarDTO, calendar)) {
					numberOfJobs += 1;
				}
			}
			// jobCountMap.put(getFormattedDate(calendarDTO.getTime()) + " ", numberOfJobs);
			list.add(numberOfJobs);
			calendarDTO.add(Calendar.DATE, 1);
		}
		return ResponseEntity.ok(list);
	}

	private boolean isSameDay(Calendar cal1, Calendar cal2) {
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
	}

	private String getFormattedDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.format(date);
	}
}

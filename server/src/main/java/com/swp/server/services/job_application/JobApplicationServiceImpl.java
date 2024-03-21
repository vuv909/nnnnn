package com.swp.server.services.job_application;

import com.swp.server.dto.*;
import com.swp.server.entities.*;
import com.swp.server.repository.*;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {
	@Autowired
	private AccountRepo accountRepo;
	@Autowired
	private ProfileRepo profileRepo;
	@Autowired
	private JobRepo jobRepo;
	@Autowired
	private JobApplyRepo jobApplyRepo;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private JavaMailSenderImpl javaMailSenderImp;

	@Override
	public ResponseEntity<?> sendMailInterview(InterviewDTO jobApplyDTO) {
		try {
			String subject = "Thư mời tham gia phỏng vấn";
			String senderName = "JobCompany";
			String mailContent = "<h1>Thư mời phỏng vấn công việc " + jobApplyDTO.getJobName() + "</h1>";
			mailContent += "<p>Lời đầu tiên, chúng tôi xin cảm ơn bạn đã quan tâm đến cơ hội nghề nghiệp tại công ty chúng tôi.</p>";
			mailContent += "<p>Sau khi xem xét hồ sơ của bạn, phòng Tuyển dụng của công ty trân trọng mời bạn tham dự Vòng Phỏng vấn công việc "
					+ jobApplyDTO.getJobName() + " của công ty.</p>";
			mailContent += "<p>Thông tin buổi phỏng vấn của bạn như sau:</p>";
			mailContent += "<p>Thời gian:" + jobApplyDTO.getTime() + "," + jobApplyDTO.getDate() + "</p>";
			mailContent += "<p>Địa điểm:" + jobApplyDTO.getAddress() + "</p>";
			mailContent += "<p>Người hỗ trợ:" + jobApplyDTO.getSupporter() + "</p>";
			mailContent += "<p>Thank you and Warmest Regards,</p>";
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom("thaimih834@gmail.com", senderName);
			helper.setTo(jobApplyDTO.getEmail());
			helper.setSubject(subject);
			helper.setText(mailContent, true);

			Properties props = javaMailSenderImp.getJavaMailProperties();
			props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

			javaMailSender.send(message);

			Map<String, String> success = new HashMap<String, String>();
			success.put("success", "Send interview calendar successfully !!!");
			return new ResponseEntity<>(success, HttpStatus.ACCEPTED);
		} catch (MessagingException | UnsupportedEncodingException e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> applyJob(@ModelAttribute JobApplyDTO jobApplyDTO) {
		try {
			System.out.println("email:" + jobApplyDTO.getEmail());
//             System.out.println("apply id:" + jobApplyDTO.getId());
			Optional<Account> findAccountByEmail = accountRepo.findFirstByEmail(jobApplyDTO.getEmail());
			Optional<Job> findJobById = jobRepo.findById(jobApplyDTO.getJob_Id());

			if (findAccountByEmail.isEmpty()) {
				Map<String, String> error = new HashMap<>();
				error.put("error", "Account not found");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}
			if (findJobById.isEmpty()) {
				Map<String, String> error = new HashMap<>();
				error.put("error", "Job not found");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}

			Account account = findAccountByEmail.get();
			System.out.println("account id:" + account.getId());
			Optional<Profile> findProfileByAccountId = profileRepo.findFirstByAccount_id(account.getId());
			Profile profile = findProfileByAccountId.get();
			if (profile.getCV() == null || profile.getCV().length == 0) {
				Map<String, String> error = new HashMap<>();
				error.put("error", "Vui lòng vào trang profile cập nhật CV !!!");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}
			Job job = findJobById.get();
			System.out.println("job id:" + job.getId());
			List<Job_Application> appliedJob = jobApplyRepo.findByAccountId(account.getId());
			for (Job_Application ja : appliedJob) {
				if (ja.getStatus().equalsIgnoreCase("accept")) {
					Map<String, String> error = new HashMap<>();
					error.put("error", "You have accepted before");
					return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
				}
			}
			// Check if the user has already applied for the same job more than three times
			int appliedTimes = jobApplyRepo.countByJobAndEmail(jobApplyDTO.getJob_Id(), jobApplyDTO.getEmail());
			System.out.println("Count apply: " + appliedTimes);
			if (appliedTimes >= 3) {
				Map<String, String> error = new HashMap<>();
				error.put("error", "You have already applied for this job more than three times");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			Job_Application jobApplication = new Job_Application();
			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			jobApplication.setEmail(jobApplyDTO.getEmail());
			jobApplication.setHr_Email(job.getHrEmail());
			jobApplication.setFull_Name(profile.getFirstName() + profile.getLastName());
			jobApplication.setStatus("Pending");
			jobApplication.setCover_Letter(jobApplyDTO.getCover_Letter());
			jobApplication.setCreated_At(sqlDate);
			jobApplication.setPhone_Number(profile.getPhoneNumber());
			jobApplication.setCV(profile.getCV());
			jobApplication.setJob(job);
			jobApplication.setAccount(account);
			Job_Application newJob_application = jobApplyRepo.save(jobApplication);
			Map<String, String> success = new HashMap<String, String>();
			success.put("success", "Apply a job successfully !!!");
			return new ResponseEntity<>(success, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> updateJobApplication(@ModelAttribute UpdateJobApplyDTO updateJobApplyDTO) {
		try {
			Optional<Job_Application> findJobApplyById = jobApplyRepo.findFirstById(updateJobApplyDTO.getId());
			if (findJobApplyById.isEmpty()) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Job application not found");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

			Job_Application jobApplication = findJobApplyById.get();
			System.out.println("jobApply ID: " + jobApplication.getId());
			System.out.println("jobApply status before: " + jobApplication.getStatus());
			if (updateJobApplyDTO.getStatus() != null) {
				jobApplication.setStatus(updateJobApplyDTO.getStatus()); // Update the status field
				Date date = new Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				jobApplication.setUpdated_At(sqlDate);
			}
			System.out.println("jobApply status after: " + jobApplication.getStatus());

			// Save the updated job application
			jobApplyRepo.save(jobApplication);
			Optional<Account> jobSeekerAccount = accountRepo.findFirstByEmail(jobApplication.getEmail());
			if (jobSeekerAccount.isEmpty()) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Account not found");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

			List<Job_Application> accountApplies = jobApplyRepo.findByAccountId(jobSeekerAccount.get().getId());
			if (updateJobApplyDTO.getStatus().equalsIgnoreCase("accept")) {
				for (Job_Application ja : accountApplies) {
					if (ja.getStatus().equalsIgnoreCase("pending")) {
						ja.setStatus("AppliedBefore");
						jobApplyRepo.save(ja);
					}
				}
			}

			Map<String, String> success = new HashMap<String, String>();
			success.put("success", "Update a job application successfully !!!");
			return new ResponseEntity<>(success, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> viewJobApplicationByAccountId(String email, int page) {
		try {
			Optional<Account> findAccountId = accountRepo.findFirstByEmail(email);
			List<ViewJobApplyDTO> viewJobApplyDTOList = new ArrayList<>();
			if (findAccountId.isEmpty()) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "AccountId not found");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}

			List<Job_Application> findJobAppliByAccountId = jobApplyRepo.findByAccountId(findAccountId.get().getId());

			if (findJobAppliByAccountId.isEmpty()) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Job application not found");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}
			for (Job_Application jobApplication : findJobAppliByAccountId) {
				if (jobApplication.getStatus().equalsIgnoreCase("Reject")
						|| jobApplication.getStatus().equalsIgnoreCase("Pending")
						|| jobApplication.getStatus().equalsIgnoreCase("Accept")
						|| jobApplication.getStatus().equalsIgnoreCase("Pass")
						|| jobApplication.getStatus().equalsIgnoreCase("Notpass")) {
					ViewJobApplyDTO viewJobApplyDTO = new ViewJobApplyDTO();

					Optional<Job> optionalJob = jobRepo.findById(jobApplication.getJob().getId());
					if (optionalJob.isEmpty()) {
						Map<String, Object> error = new HashMap<>();
						error.put("error", "Job not found");
						return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
					}
					LocalDate applyBeforeDate = jobApplication.getJob().getApply_Before().toLocalDate();

					// Check if the job is expired or valid based on applyBefore date
					if (LocalDate.now().isAfter(applyBeforeDate)) {
						viewJobApplyDTO.setStatusActive(false);
					} else {
						viewJobApplyDTO.setStatusActive(true);
					}
					viewJobApplyDTO.setJobId(optionalJob.get().getId());
					viewJobApplyDTO.setJobName(optionalJob.get().getName());
					viewJobApplyDTO.setId(jobApplication.getId());
					viewJobApplyDTO.setEmail(jobApplication.getEmail());
					viewJobApplyDTO.setEmailAccount(findAccountId.get().getEmail());
					viewJobApplyDTO.setFull_Name(jobApplication.getFull_Name());
					viewJobApplyDTO.setCV(jobApplication.getCV());
					viewJobApplyDTO.setStatus(jobApplication.getStatus());
					viewJobApplyDTO.setCover_Letter(jobApplication.getCover_Letter());
					viewJobApplyDTO.setCreated_At(jobApplication.getCreated_At());
					viewJobApplyDTO.setUpdated_At(jobApplication.getUpdated_At());
					viewJobApplyDTO.setPhone_Number(jobApplication.getPhone_Number());
					viewJobApplyDTOList.add(viewJobApplyDTO);
				}
			}

			int totalJobs = viewJobApplyDTOList.size();
			int limit = 5; // Number of jobs per page
			int totalPages = (int) Math.ceil((double) totalJobs / limit);
			int startIndex = (page - 1) * limit;
			int endIndex = Math.min(startIndex + limit, totalJobs);

			List<ViewJobApplyDTO> paginatedJobs = viewJobApplyDTOList.subList(startIndex, endIndex);

			// Prepare DTO for response
			ResultPaginationDTO<ViewJobApplyDTO> dto = new ResultPaginationDTO<>();
			dto.setPage(page);
			dto.setListResults(paginatedJobs);
			dto.setTotalPage(totalPages);

			return ResponseEntity.ok(dto);

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> viewJobApplicationByHrEmail(String email, String status, Integer page) {
		try {

			List<ViewJobApplyDTO> viewJobApplyDTOList = new ArrayList<>();

			List<Job_Application> findJobAppliByAccountId = jobApplyRepo.findByHrEmail(email);

			if (findJobAppliByAccountId.isEmpty()) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Job application not found");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}

			if (status.equalsIgnoreCase("Accept")) {
				for (Job_Application jobApplication : findJobAppliByAccountId) {
					if (jobApplication.getStatus().equalsIgnoreCase(status)) {
						LocalDate applyBeforeDate = jobApplication.getJob().getApply_Before().toLocalDate();

						ViewJobApplyDTO viewJobApplyDTO = new ViewJobApplyDTO();

						Optional<Job> optionalJob = jobRepo.findById(jobApplication.getJob().getId());
						if (optionalJob.isEmpty()) {
							Map<String, Object> error = new HashMap<>();
							error.put("error", "Job not found");
							return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
						}

						Optional<Account> findAccountId = accountRepo.findById(jobApplication.getAccount().getId());
						if (findAccountId.isEmpty()) {
							Map<String, Object> error = new HashMap<>();
							error.put("error", "Account not found");
							return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
						}
						viewJobApplyDTO.setJobName(optionalJob.get().getName());
						viewJobApplyDTO.setId(jobApplication.getId());
						viewJobApplyDTO.setEmail(jobApplication.getEmail());
						viewJobApplyDTO.setEmailAccount(findAccountId.get().getEmail());
						viewJobApplyDTO.setFull_Name(jobApplication.getFull_Name());
						viewJobApplyDTO.setCV(jobApplication.getCV());
						viewJobApplyDTO.setStatus(jobApplication.getStatus());
						viewJobApplyDTO.setCover_Letter(jobApplication.getCover_Letter());
						viewJobApplyDTO.setCreated_At(jobApplication.getCreated_At());
						viewJobApplyDTO.setUpdated_At(jobApplication.getUpdated_At());
						viewJobApplyDTO.setPhone_Number(jobApplication.getPhone_Number());
						viewJobApplyDTOList.add(viewJobApplyDTO);
					}
				}
				if (page == null) {
					return ResponseEntity.ok(viewJobApplyDTOList);
				}
				int totalJobs = viewJobApplyDTOList.size();
				int limit = 5; // Number of jobs per page
				int totalPages = (int) Math.ceil((double) totalJobs / limit);
				int startIndex = (page - 1) * limit;
				int endIndex = Math.min(startIndex + limit, totalJobs);

				List<ViewJobApplyDTO> paginatedJobs = viewJobApplyDTOList.subList(startIndex, endIndex);

				// Prepare DTO for response
				ResultPaginationDTO<ViewJobApplyDTO> dto = new ResultPaginationDTO<>();
				dto.setPage(page);
				dto.setListResults(paginatedJobs);
				dto.setTotalPage(totalPages);

				return ResponseEntity.ok(dto);
			}

			else if (status.equalsIgnoreCase("PASS")) {
				for (Job_Application jobApplication : findJobAppliByAccountId) {
					if (jobApplication.getStatus().equalsIgnoreCase(status)) {
						LocalDate applyBeforeDate = jobApplication.getJob().getApply_Before().toLocalDate();

						ViewJobApplyDTO viewJobApplyDTO = new ViewJobApplyDTO();

						Optional<Job> optionalJob = jobRepo.findById(jobApplication.getJob().getId());
						if (optionalJob.isEmpty()) {
							Map<String, Object> error = new HashMap<>();
							error.put("error", "Job not found");
							return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
						}

						Optional<Account> findAccountId = accountRepo.findById(jobApplication.getAccount().getId());
						if (findAccountId.isEmpty()) {
							Map<String, Object> error = new HashMap<>();
							error.put("error", "Account not found");
							return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
						}
						viewJobApplyDTO.setJobName(optionalJob.get().getName());
						viewJobApplyDTO.setId(jobApplication.getId());
						viewJobApplyDTO.setEmail(jobApplication.getEmail());
						viewJobApplyDTO.setEmailAccount(findAccountId.get().getEmail());
						viewJobApplyDTO.setFull_Name(jobApplication.getFull_Name());
						viewJobApplyDTO.setCV(jobApplication.getCV());
						viewJobApplyDTO.setStatus(jobApplication.getStatus());
						viewJobApplyDTO.setCover_Letter(jobApplication.getCover_Letter());
						viewJobApplyDTO.setCreated_At(jobApplication.getCreated_At());
						viewJobApplyDTO.setUpdated_At(jobApplication.getUpdated_At());
						viewJobApplyDTO.setPhone_Number(jobApplication.getPhone_Number());
						viewJobApplyDTOList.add(viewJobApplyDTO);
					}
				}
				if (page == null) {
					return ResponseEntity.ok(viewJobApplyDTOList);
				}
				int totalJobs = viewJobApplyDTOList.size();
				int limit = 5; // Number of jobs per page
				int totalPages = (int) Math.ceil((double) totalJobs / limit);
				int startIndex = (page - 1) * limit;
				int endIndex = Math.min(startIndex + limit, totalJobs);

				List<ViewJobApplyDTO> paginatedJobs = viewJobApplyDTOList.subList(startIndex, endIndex);

				// Prepare DTO for response
				ResultPaginationDTO<ViewJobApplyDTO> dto = new ResultPaginationDTO<>();
				dto.setPage(page);
				dto.setListResults(paginatedJobs);
				dto.setTotalPage(totalPages);

				return ResponseEntity.ok(dto);
			}

			else if (status.equalsIgnoreCase("NOTPASS")) {
				for (Job_Application jobApplication : findJobAppliByAccountId) {
					if (jobApplication.getStatus().equalsIgnoreCase(status)) {
						LocalDate applyBeforeDate = jobApplication.getJob().getApply_Before().toLocalDate();

						ViewJobApplyDTO viewJobApplyDTO = new ViewJobApplyDTO();

						Optional<Job> optionalJob = jobRepo.findById(jobApplication.getJob().getId());
						if (optionalJob.isEmpty()) {
							Map<String, Object> error = new HashMap<>();
							error.put("error", "Job not found");
							return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
						}

						Optional<Account> findAccountId = accountRepo.findById(jobApplication.getAccount().getId());
						if (findAccountId.isEmpty()) {
							Map<String, Object> error = new HashMap<>();
							error.put("error", "Account not found");
							return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
						}
						viewJobApplyDTO.setJobName(optionalJob.get().getName());
						viewJobApplyDTO.setId(jobApplication.getId());
						viewJobApplyDTO.setEmail(jobApplication.getEmail());
						viewJobApplyDTO.setEmailAccount(findAccountId.get().getEmail());
						viewJobApplyDTO.setFull_Name(jobApplication.getFull_Name());
						viewJobApplyDTO.setCV(jobApplication.getCV());
						viewJobApplyDTO.setStatus(jobApplication.getStatus());
						viewJobApplyDTO.setCover_Letter(jobApplication.getCover_Letter());
						viewJobApplyDTO.setCreated_At(jobApplication.getCreated_At());
						viewJobApplyDTO.setUpdated_At(jobApplication.getUpdated_At());
						viewJobApplyDTO.setPhone_Number(jobApplication.getPhone_Number());
						viewJobApplyDTOList.add(viewJobApplyDTO);
					}
				}
				if (page == null) {
					return ResponseEntity.ok(viewJobApplyDTOList);
				}
				int totalJobs = viewJobApplyDTOList.size();
				int limit = 5; // Number of jobs per page
				int totalPages = (int) Math.ceil((double) totalJobs / limit);
				int startIndex = (page - 1) * limit;
				int endIndex = Math.min(startIndex + limit, totalJobs);

				List<ViewJobApplyDTO> paginatedJobs = viewJobApplyDTOList.subList(startIndex, endIndex);

				// Prepare DTO for response
				ResultPaginationDTO<ViewJobApplyDTO> dto = new ResultPaginationDTO<>();
				dto.setPage(page);
				dto.setListResults(paginatedJobs);
				dto.setTotalPage(totalPages);

				return ResponseEntity.ok(dto);
			}

			else if (status.equalsIgnoreCase("Reject")) {
				for (Job_Application jobApplication : findJobAppliByAccountId) {
					if (jobApplication.getStatus().equalsIgnoreCase(status)) {
						LocalDate applyBeforeDate = jobApplication.getJob().getApply_Before().toLocalDate();

						ViewJobApplyDTO viewJobApplyDTO = new ViewJobApplyDTO();

						Optional<Job> optionalJob = jobRepo.findById(jobApplication.getJob().getId());
						if (optionalJob.isEmpty()) {
							Map<String, Object> error = new HashMap<>();
							error.put("error", "Job not found");
							return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
						}

						Optional<Account> findAccountId = accountRepo.findById(jobApplication.getAccount().getId());
						if (findAccountId.isEmpty()) {
							Map<String, Object> error = new HashMap<>();
							error.put("error", "Account not found");
							return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
						}
						viewJobApplyDTO.setJobName(optionalJob.get().getName());
						viewJobApplyDTO.setId(jobApplication.getId());
						viewJobApplyDTO.setEmail(jobApplication.getEmail());
						viewJobApplyDTO.setEmailAccount(findAccountId.get().getEmail());
						viewJobApplyDTO.setFull_Name(jobApplication.getFull_Name());
						viewJobApplyDTO.setCV(jobApplication.getCV());
						viewJobApplyDTO.setStatus(jobApplication.getStatus());
						viewJobApplyDTO.setCover_Letter(jobApplication.getCover_Letter());
						viewJobApplyDTO.setCreated_At(jobApplication.getCreated_At());
						viewJobApplyDTO.setUpdated_At(jobApplication.getUpdated_At());
						viewJobApplyDTO.setPhone_Number(jobApplication.getPhone_Number());
						viewJobApplyDTOList.add(viewJobApplyDTO);
					}
				}
				if (page == null) {
					return ResponseEntity.ok(viewJobApplyDTOList);
				}
				int totalJobs = viewJobApplyDTOList.size();
				int limit = 5; // Number of jobs per page
				int totalPages = (int) Math.ceil((double) totalJobs / limit);
				int startIndex = (page - 1) * limit;
				int endIndex = Math.min(startIndex + limit, totalJobs);

				List<ViewJobApplyDTO> paginatedJobs = viewJobApplyDTOList.subList(startIndex, endIndex);

				// Prepare DTO for response
				ResultPaginationDTO<ViewJobApplyDTO> dto = new ResultPaginationDTO<>();
				dto.setPage(page);
				dto.setListResults(paginatedJobs);
				dto.setTotalPage(totalPages);

				return ResponseEntity.ok(dto);
			} else if (status.equalsIgnoreCase("Pending")) {
				for (Job_Application jobApplication : findJobAppliByAccountId) {
					LocalDate applyBeforeDate = jobApplication.getJob().getApply_Before().toLocalDate();
					if (jobApplication.getStatus().equalsIgnoreCase(status)
							&& LocalDate.now().isAfter(applyBeforeDate) == false) {
						ViewJobApplyDTO viewJobApplyDTO = new ViewJobApplyDTO();

						Optional<Job> optionalJob = jobRepo.findById(jobApplication.getJob().getId());
						if (optionalJob.isEmpty()) {
							Map<String, Object> error = new HashMap<>();
							error.put("error", "Job not found");
							return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
						}

						Optional<Account> findAccountId = accountRepo.findById(jobApplication.getAccount().getId());
						if (findAccountId.isEmpty()) {
							Map<String, Object> error = new HashMap<>();
							error.put("error", "Account not found");
							return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
						}
						viewJobApplyDTO.setJobName(optionalJob.get().getName());
						viewJobApplyDTO.setId(jobApplication.getId());
						viewJobApplyDTO.setEmail(jobApplication.getEmail());
						viewJobApplyDTO.setEmailAccount(findAccountId.get().getEmail());
						viewJobApplyDTO.setFull_Name(jobApplication.getFull_Name());
						viewJobApplyDTO.setCV(jobApplication.getCV());
						viewJobApplyDTO.setStatus(jobApplication.getStatus());
						viewJobApplyDTO.setCover_Letter(jobApplication.getCover_Letter());
						viewJobApplyDTO.setCreated_At(jobApplication.getCreated_At());
						viewJobApplyDTO.setUpdated_At(jobApplication.getUpdated_At());
						viewJobApplyDTO.setPhone_Number(jobApplication.getPhone_Number());
						viewJobApplyDTOList.add(viewJobApplyDTO);
					}
				}
				if (page == null) {
					return ResponseEntity.ok(viewJobApplyDTOList);
				}
				int totalJobs = viewJobApplyDTOList.size();
				int limit = 5; // Number of jobs per page
				int totalPages = (int) Math.ceil((double) totalJobs / limit);
				int startIndex = (page - 1) * limit;
				int endIndex = Math.min(startIndex + limit, totalJobs);

				List<ViewJobApplyDTO> paginatedJobs = viewJobApplyDTOList.subList(startIndex, endIndex);

				// Prepare DTO for response
				ResultPaginationDTO<ViewJobApplyDTO> dto = new ResultPaginationDTO<>();
				dto.setPage(page);
				dto.setListResults(paginatedJobs);
				dto.setTotalPage(totalPages);

				return ResponseEntity.ok(dto);
			} else if (status.equalsIgnoreCase("EXPIRED")) {
				for (Job_Application jobApplication : findJobAppliByAccountId) {

					LocalDate applyBeforeDate = jobApplication.getJob().getApply_Before().toLocalDate();

					if (LocalDate.now().isAfter(applyBeforeDate) == true) {

						ViewJobApplyDTO viewJobApplyDTO = new ViewJobApplyDTO();

						Optional<Job> optionalJob = jobRepo.findById(jobApplication.getJob().getId());
						if (optionalJob.isEmpty()) {
							Map<String, Object> error = new HashMap<>();
							error.put("error", "Job not found");
							return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
						}

						Optional<Account> findAccountId = accountRepo.findById(jobApplication.getAccount().getId());
						if (findAccountId.isEmpty()) {
							Map<String, Object> error = new HashMap<>();
							error.put("error", "Account not found");
							return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
						}

						viewJobApplyDTO.setJobName(optionalJob.get().getName());
						viewJobApplyDTO.setId(jobApplication.getId());
						viewJobApplyDTO.setEmail(jobApplication.getEmail());
						viewJobApplyDTO.setEmailAccount(findAccountId.get().getEmail());
						viewJobApplyDTO.setFull_Name(jobApplication.getFull_Name());
						viewJobApplyDTO.setCV(jobApplication.getCV());
						viewJobApplyDTO.setStatus(jobApplication.getStatus());
						viewJobApplyDTO.setCover_Letter(jobApplication.getCover_Letter());
						viewJobApplyDTO.setCreated_At(jobApplication.getCreated_At());
						viewJobApplyDTO.setUpdated_At(jobApplication.getUpdated_At());
						viewJobApplyDTO.setPhone_Number(jobApplication.getPhone_Number());
						viewJobApplyDTOList.add(viewJobApplyDTO);
					}
				}
				if (page == null) {
					return ResponseEntity.ok(viewJobApplyDTOList);
				}
				int totalJobs = viewJobApplyDTOList.size();
				int limit = 5; // Number of jobs per page
				int totalPages = (int) Math.ceil((double) totalJobs / limit);
				int startIndex = (page - 1) * limit;
				int endIndex = Math.min(startIndex + limit, totalJobs);

				List<ViewJobApplyDTO> paginatedJobs = viewJobApplyDTOList.subList(startIndex, endIndex);

				// Prepare DTO for response
				ResultPaginationDTO<ViewJobApplyDTO> dto = new ResultPaginationDTO<>();
				dto.setPage(page);
				dto.setListResults(paginatedJobs);
				dto.setTotalPage(totalPages);

				return ResponseEntity.ok(dto);
			}

			for (Job_Application jobApplication : findJobAppliByAccountId) {

				LocalDate applyBeforeDate = jobApplication.getJob().getApply_Before().toLocalDate();

				if (LocalDate.now().isAfter(applyBeforeDate) == true) {

					ViewJobApplyDTO viewJobApplyDTO = new ViewJobApplyDTO();

					Optional<Job> optionalJob = jobRepo.findById(jobApplication.getJob().getId());
					if (optionalJob.isEmpty()) {
						Map<String, Object> error = new HashMap<>();
						error.put("error", "Job not found");
						return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
					}

					Optional<Account> findAccountId = accountRepo.findById(jobApplication.getAccount().getId());
					if (findAccountId.isEmpty()) {
						Map<String, Object> error = new HashMap<>();
						error.put("error", "Account not found");
						return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
					}

					viewJobApplyDTO.setJobName(optionalJob.get().getName());
					viewJobApplyDTO.setId(jobApplication.getId());
					viewJobApplyDTO.setEmail(jobApplication.getEmail());
					viewJobApplyDTO.setEmailAccount(findAccountId.get().getEmail());
					viewJobApplyDTO.setFull_Name(jobApplication.getFull_Name());
					viewJobApplyDTO.setCV(jobApplication.getCV());
					viewJobApplyDTO.setStatus(jobApplication.getStatus());
					viewJobApplyDTO.setCover_Letter(jobApplication.getCover_Letter());
					viewJobApplyDTO.setCreated_At(jobApplication.getCreated_At());
					viewJobApplyDTO.setUpdated_At(jobApplication.getUpdated_At());
					viewJobApplyDTO.setPhone_Number(jobApplication.getPhone_Number());
					viewJobApplyDTOList.add(viewJobApplyDTO);
				}
			}
			if (page == null) {
				return ResponseEntity.ok(viewJobApplyDTOList);
			}
			int totalJobs = viewJobApplyDTOList.size();
			int limit = 5; // Number of jobs per page
			int totalPages = (int) Math.ceil((double) totalJobs / limit);
			int startIndex = (page - 1) * limit;
			int endIndex = Math.min(startIndex + limit, totalJobs);

			List<ViewJobApplyDTO> paginatedJobs = viewJobApplyDTOList.subList(startIndex, endIndex);

			// Prepare DTO for response
			ResultPaginationDTO<ViewJobApplyDTO> dto = new ResultPaginationDTO<>();
			dto.setPage(page);
			dto.setListResults(paginatedJobs);
			dto.setTotalPage(totalPages);

			return ResponseEntity.ok(dto);

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> viewJobApplication(Integer page) {
		try {
			List<Job_Application> jobApplicationList = jobApplyRepo.findAll();
			List<ViewJobApplyDTO> viewJobApplyDTOList = new ArrayList<>();
			if (jobApplicationList.isEmpty()) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "No job applications found");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}
			for (Job_Application jobApplication : jobApplicationList) {
				if (jobApplication.getStatus().equalsIgnoreCase("Reject")
						|| jobApplication.getStatus().equalsIgnoreCase("Pending")
						|| jobApplication.getStatus().equalsIgnoreCase("Accept")
						|| jobApplication.getStatus().equalsIgnoreCase("Pass")
						|| jobApplication.getStatus().equalsIgnoreCase("Notpass")) {

					ViewJobApplyDTO viewJobApplyDTO = new ViewJobApplyDTO();

					Optional<Job> optionalJob = jobRepo.findById(jobApplication.getJob().getId());
					if (optionalJob.isEmpty()) {
						Map<String, Object> error = new HashMap<>();
						error.put("error", "Job not found");
						return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
					}

					Optional<Account> findAccountId = accountRepo.findById(jobApplication.getAccount().getId());
					if (findAccountId.isEmpty()) {
						Map<String, Object> error = new HashMap<>();
						error.put("error", "Account not found");
						return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
					}

					viewJobApplyDTO.setJobName(optionalJob.get().getName());
					viewJobApplyDTO.setId(jobApplication.getId());
					viewJobApplyDTO.setEmail(jobApplication.getEmail());
					viewJobApplyDTO.setEmailAccount(findAccountId.get().getEmail());
					viewJobApplyDTO.setFull_Name(jobApplication.getFull_Name());
					viewJobApplyDTO.setCV(jobApplication.getCV());
					viewJobApplyDTO.setStatus(jobApplication.getStatus());
					viewJobApplyDTO.setCover_Letter(jobApplication.getCover_Letter());
					viewJobApplyDTO.setCreated_At(jobApplication.getCreated_At());
					viewJobApplyDTO.setUpdated_At(jobApplication.getUpdated_At());
					viewJobApplyDTO.setPhone_Number(jobApplication.getPhone_Number());
					viewJobApplyDTOList.add(viewJobApplyDTO);
				}
			}
			if (page == null) {
				return ResponseEntity.ok(viewJobApplyDTOList);
			}
			int totalJobs = viewJobApplyDTOList.size();
			int limit = 5; // Number of jobs per page
			int totalPages = (int) Math.ceil((double) totalJobs / limit);
			int startIndex = (page - 1) * limit;
			int endIndex = Math.min(startIndex + limit, totalJobs);

			List<ViewJobApplyDTO> paginatedJobs = viewJobApplyDTOList.subList(startIndex, endIndex);

			// Prepare DTO for response
			ResultPaginationDTO<ViewJobApplyDTO> dto = new ResultPaginationDTO<>();
			dto.setPage(page);
			dto.setListResults(paginatedJobs);
			dto.setTotalPage(totalPages);

			return ResponseEntity.ok(dto);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

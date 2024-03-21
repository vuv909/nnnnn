package com.swp.server.services.favorite_job;

import com.swp.server.dto.Favorite_JobDTO;
import com.swp.server.dto.ResultPaginationDTO;
import com.swp.server.dto.ViewJobApplyDTO;
import com.swp.server.entities.Account;
import com.swp.server.entities.Favorite_Job;
import com.swp.server.entities.Job;
import com.swp.server.repository.AccountRepo;
import com.swp.server.repository.FavoriteJobRepo;
import com.swp.server.repository.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class FavoriteJobServiceImpl implements FavoriteJobService {

	@Autowired
	private FavoriteJobRepo favoriteJobRepo;
	@Autowired
	private AccountRepo accountRepo;
	@Autowired
	private JobRepo jobRepo;

	@Override
	public ResponseEntity<?> favoriteJob(Favorite_JobDTO favorite_jobDTO) {
		try {
			// Find the account by ID
			System.out.println("idaccount::" + favorite_jobDTO.getAccountId());
			Optional<Account> findAccountId = accountRepo.findById(favorite_jobDTO.getAccountId());
			if (findAccountId.isEmpty()) {
				Map<String, String> error = new HashMap<>();
				error.put("error", "Account not found!");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}
			Account account = findAccountId.get();

			// Find the job by ID

			Optional<Job> findJobId = jobRepo.findById(favorite_jobDTO.getJobId());
			if (findJobId.isEmpty()) {
				Map<String, String> error = new HashMap<>();
				error.put("error", "Job not found!");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}
			Job job = findJobId.get();

			// Check if the job is already in favorites for the account
			List<Favorite_Job> favoriteJobs = favoriteJobRepo.findByAccountAndJob(account, job);
			if (!favoriteJobs.isEmpty()) {
				Map<String, String> error = new HashMap<>();
				error.put("error", "Job already exists in favorites!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

			// Create a new Favorite_Job object and save it
			Favorite_Job favoriteJob = new Favorite_Job();
			favoriteJob.setAccount(account);
			favoriteJob.setJob(job);
			Favorite_Job favorite_JobNew = favoriteJobRepo.save(favoriteJob);

			Map<String, String> success = new HashMap<>();
			success.put("id", String.valueOf(favorite_JobNew.getId()));
			success.put("success", "Added a job to your favorites successfully!");
			return new ResponseEntity<>(success, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> viewFavoriteJob(int accountId, Integer page) {
		List<Favorite_Job> favoriteJobs = favoriteJobRepo.findByAccount_Id(accountId);
		System.out.println("account id= " + accountId);
		if (favoriteJobs.isEmpty()) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "No favorite jobs found for the given account ID!");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		} else {
			Map<String, Object> success = new HashMap<>();
			success.put("success", "Get favorite jobs successfully!");

			List<Favorite_JobDTO> responseDTOs = new ArrayList<>();
			for (Favorite_Job favoriteJob : favoriteJobs) {
				Favorite_JobDTO responseDTO = new Favorite_JobDTO();
				Optional<Job> job = jobRepo.findById(favoriteJob.getJob().getId());
				if (job.isEmpty()) {
					Map<String, String> error = new HashMap<>();
					error.put("error", "Job not found !");
					return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
				}
				responseDTO.setId(favoriteJob.getId());
				responseDTO.setNameJob(job.get().getName());
				responseDTO.setAccountId(favoriteJob.getAccount().getId());
				responseDTO.setJobId(favoriteJob.getJob().getId());
				responseDTOs.add(responseDTO);
			}
			if (page != null) {
				int totalJobs = responseDTOs.size();
				int limit = 5; // Number of jobs per page
				int totalPages = (int) Math.ceil((double) totalJobs / limit);
				int startIndex = (page - 1) * limit;
				int endIndex = Math.min(startIndex + limit, totalJobs);

				List<Favorite_JobDTO> paginatedJobs = responseDTOs.subList(startIndex, endIndex);

				// Prepare DTO for response
				ResultPaginationDTO<Favorite_JobDTO> dto = new ResultPaginationDTO<>();
				dto.setPage(page);
				dto.setListResults(paginatedJobs);
				dto.setTotalPage(totalPages);

				return ResponseEntity.ok(dto);
			} else {
				success.put("metadata", responseDTOs);
				return ResponseEntity.ok(success);

			}

		}
	}

	@Override
	public ResponseEntity<?> deleteFavoriteJob(int favoriteJobId) {
		// Assuming you have a service or repository to handle the deletion
		Optional<Favorite_Job> deleteJob = favoriteJobRepo.findFirstById(favoriteJobId);

		if (deleteJob.isPresent()) {
			favoriteJobRepo.delete(deleteJob.get());

			if (deleteJob.isPresent()) {
				System.out.println("Favorite ID: " + deleteJob.get().getId());
				favoriteJobRepo.delete(deleteJob.get());

				Map<String, String> response = new HashMap<>();
				response.put("message", "Favorite job deleted successfully");
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favorite job not found");
			}
		}
		Map<String, String> error = new HashMap<>();
		error.put("error", "No favorite jobs found for the given favourite job!");
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
}

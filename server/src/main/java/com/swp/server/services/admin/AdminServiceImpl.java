package com.swp.server.services.admin;

import com.swp.server.dto.*;
import com.swp.server.entities.Account;
import com.swp.server.entities.Job;
import com.swp.server.entities.Profile;
import com.swp.server.entities.Role;
import com.swp.server.enums.AccountRole;
import com.swp.server.repository.AccountRepo;
import com.swp.server.repository.ProfileRepo;
import com.swp.server.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private AccountRepo accountRepo;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ProfileRepo profileRepo;

	@Override
	public ResponseEntity<?> viewListAccounts(ViewListAccountDTO viewListAccountDTO, Integer page) {
		try {
			List<Account> accounts = new ArrayList<>();
			if (viewListAccountDTO.isEnabled()) {
				accounts = accountRepo.findByEnabledTrue();
			} else {
				accounts = accountRepo.findByEnabledFalse();
			}
			if (accounts.isEmpty()) {
				Map<String, String> error = new HashMap<String, String>();
				error.put("error", "There's nothing to display");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}
			List<ResponseProfileDTO> responseProfileDTO = new ArrayList<>();
			for (Account a : accounts) {
				if (!a.getRole().getName().equals("ADMIN")) {
					Profile profile = a.getProfile();
					if (profile == null) {
						Map<String, String> error = new HashMap<>();
						error.put("error", "Profile not found!");
						return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
					}
					ResponseProfileDTO profileResponse = getResponseProfileDTO(a, profile);

					responseProfileDTO.add(profileResponse);
				}

			}
			if (page == null) {
				return ResponseEntity.ok(responseProfileDTO);
			}

			int totalJobs = responseProfileDTO.size();
			int limit = 5; // Number of jobs per page
			int totalPages = (int) Math.ceil((double) totalJobs / limit);
			int startIndex = (page - 1) * limit;
			int endIndex = Math.min(startIndex + limit, totalJobs);

			List<ResponseProfileDTO> paginatedJobs = responseProfileDTO.subList(startIndex, endIndex);

			// Prepare DTO for response
			ResultPaginationDTO<ResponseProfileDTO> dto = new ResultPaginationDTO<>();
			dto.setPage(page);
			dto.setListResults(paginatedJobs);
			dto.setTotalPage(totalPages);

			return ResponseEntity.ok(dto);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.ACCEPTED);
		}
	}

	private static ResponseProfileDTO getResponseProfileDTO(Account a, Profile profile) {
		ResponseProfileDTO profileResponse = new ResponseProfileDTO();

		profileResponse.setRole(a.getRole().getName());
		profileResponse.setUsername(a.getUsername());
		profileResponse.setVerify(a.isVerify());
		profileResponse.setEnabled(a.isEnabled());

		profileResponse.setAddress(profile.getAddress());
		profileResponse.setEmail(a.getEmail());
		profileResponse.setFirstName(profile.getFirstName());
		profileResponse.setLastName(profile.getLastName());
		profileResponse.setPhoneNumber(profile.getPhoneNumber());
		profileResponse.setGender(profile.isGender());
		profileResponse.setAvatar(profile.getAvatar());
		return profileResponse;
	}

	@Override
	public ResponseEntity<?> createAccount(CreateAccountDTO createAccountDTO) {
		try {
			CheckMailDTO response = restTemplate.getForObject(
					"https://emailverification.whoisxmlapi.com/api/v3?apiKey=at_diEFn6Z2zEs4QNnPquwGEYcUKDm3D&emailAddress="
							+ createAccountDTO.getEmail(),
					CheckMailDTO.class);
			if (response != null && response.isSmtpCheck()) {
				Optional<Account> checkUsernameExist = accountRepo.findFirstByUsername(createAccountDTO.getUsername());
				if (checkUsernameExist.isPresent()) {
					Map<String, String> error = new HashMap<String, String>();
					error.put("error", "Username is existed  !!!");
					return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
				}

				Optional<Account> checkEmailExist = accountRepo.findFirstByEmail(createAccountDTO.getEmail());
				if (checkEmailExist.isPresent()) {
					Map<String, String> error = new HashMap<String, String>();
					error.put("error", "Email is existed  !!!");
					return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
				}

				String passwordRegex = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
				Pattern patternPassword = Pattern.compile(passwordRegex);
				Matcher matcherPassword = patternPassword.matcher(createAccountDTO.getPassword());
				if (!matcherPassword.matches()) {
					Map<String, String> error = new HashMap<String, String>();
					error.put("error",
							"Password at least 8 characrter , 1 uppercase , 1 special character and should not contain any whitespace characters.  !!!");
					return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
				}

				Role roleEntity = roleRepo.findByName("EMPLOYER");
				Account newAccountEntity = new Account();
				newAccountEntity.setUsername(createAccountDTO.getUsername());
				newAccountEntity.setEmail(createAccountDTO.getEmail());
				newAccountEntity.setPassword(new BCryptPasswordEncoder().encode(createAccountDTO.getPassword()));
				newAccountEntity.setVerify(true);
				newAccountEntity.setEnabled(true);
				newAccountEntity.setRole(roleEntity);

				Profile profile = new Profile();
				profile.setAccount(newAccountEntity);
				Account newAccount = accountRepo.save(newAccountEntity);
				Profile newProfile = profileRepo.save(profile);
				Map<String, String> success = new HashMap<String, String>();
				success.put("success", "Create account successfully !!!");
				return new ResponseEntity<>(success, HttpStatus.OK);
			} else {
				Map<String, String> error = new HashMap<String, String>();
				error.put("error", "Email is not exist !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.ACCEPTED);
		}
	}

	@Override
	public ResponseEntity<?> blockAccount(BlockAccountDTO blockAccountDTO) {
		try {
			Optional<Account> checkEmailExisted = accountRepo.findFirstByEmail(blockAccountDTO.getEmail());
			if (!checkEmailExisted.isPresent()) {
				Map<String, String> error = new HashMap<String, String>();
				error.put("error", "Email is not existed  !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			Account blockAccount = checkEmailExisted.get();
			if (blockAccount.getRole().getName().toUpperCase().equals(AccountRole.ADMIN.toString())) {
				Map<String, String> error = new HashMap<String, String>();
				error.put("error", "Request is not accepted  !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			if (!blockAccountDTO.isEnabled()) {
				blockAccount.setEnabled(false);
				accountRepo.save(blockAccount);
				Map<String, Object> msg = new HashMap<>();
				msg.put("success", "Block account " + blockAccount.getEmail() + " successfully !!!");
				return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
			} else {
				blockAccount.setEnabled(true);
				accountRepo.save(blockAccount);
				Map<String, Object> msg = new HashMap<>();
				msg.put("success", "Unblock account " + blockAccount.getEmail() + " successfully !!!");
				return new ResponseEntity<>(msg, HttpStatus.ACCEPTED);
			}

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.ACCEPTED);
		}
	}

	@Override
	public ResponseEntity<?> searchAccount(SearchAccountDTO searchAccountDTO) {
		try {
			List<Account> accountsSearch = new ArrayList<>();
			if (searchAccountDTO.getTxtSearch() == null || searchAccountDTO.getTxtSearch().trim().isEmpty()) {
				if (searchAccountDTO.getRoleId() == 0) {
					accountsSearch = accountRepo.findBy();
				} else {
					Role role = roleRepo.findById(searchAccountDTO.getRoleId());
					accountsSearch = accountRepo.findByRoleAndOrderByUsername(role);
				}
			} else {
				if (searchAccountDTO.getRoleId() == 0) {
					accountsSearch = accountRepo.findAndOrderByUsername(searchAccountDTO.getTxtSearch());
				} else {
					Role role = roleRepo.findById(searchAccountDTO.getRoleId());
					accountsSearch = accountRepo
							.findByUsernameContainingAndRoleAndOrderByUsername(searchAccountDTO.getTxtSearch(), role);
				}
			}
			if (accountsSearch.isEmpty()) {
				Map<String, String> error = new HashMap<String, String>();
				error.put("error", "Account is not found !!!");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}
			List<ResponseProfileDTO> responseProfileDTO = new ArrayList<>();
			for (Account a : accountsSearch) {
				Profile profile = a.getProfile();
				if (profile == null) {
					Map<String, String> error = new HashMap<>();
					error.put("error", "Profile not found!");
					return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
				}
				ResponseProfileDTO profileResponse = getResponseProfileDTO(a, profile);
				responseProfileDTO.add(profileResponse);

			}
			return ResponseEntity.ok(responseProfileDTO);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.ACCEPTED);
		}
	}

	@Override
	public ResponseEntity<?> filterAccount(FilterAccountDTO filterAccountDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<?> countByRole() {
		List<Account> accountOptional = accountRepo.findAll().stream().toList();
		Map<String, Integer> roleCountMap = new HashMap<>();
		if (accountOptional.isEmpty()) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "Account is empty");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
		List<Role> roleList = roleRepo.findAll().stream().toList();
		for (Role role : roleList) {
			int count = 0;
			for (Account account : accountOptional) {
				if (role.getId() == account.getRole().getId()) {
					count += 1;
				}
				roleCountMap.put(role.getName() + " ", count);
			}
		}
		return ResponseEntity.ok(roleCountMap);
	}
}

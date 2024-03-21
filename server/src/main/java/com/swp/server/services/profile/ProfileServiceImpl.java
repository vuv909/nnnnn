package com.swp.server.services.profile;

import com.swp.server.dto.*;
import com.swp.server.entities.Profile;
import com.swp.server.entities.Account;
import com.swp.server.repository.AccountRepo;
import com.swp.server.repository.ProfileRepo;
import com.swp.server.services.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProfileServiceImpl implements ProfileService {
	@Autowired
	private ProfileRepo profileRepo;
	@Autowired
	private AccountRepo accountRepo;

	@Override
	public ResponseEntity<?> createProfile(ProfileDTO profileDTO) {
		try {
			Optional<Profile> checkPhoneNumberExist = profileRepo.findAllByPhoneNumber(profileDTO.getPhoneNumber());
			Optional<Account> findAccountByEmail = accountRepo.findFirstByEmail(profileDTO.getEmail());
			Account account = findAccountByEmail.get();
			Optional<Profile> findId = profileRepo.findFirstByAccount_id(account.getId());
//            if (checkPhoneNumberExist.isPresent()) {
//                Map<String, String> error = new HashMap<String, String>();
//                error.put("error", "Phone number is already registered!");
//                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//            }
			String phoneNumberRegex = "^(\\+84|0)(3[2-9]|5[2689]|7[06-9]|8[1-689]|9[0-9])\\d{7}$";
			Pattern patternPhone = Pattern.compile(phoneNumberRegex);
			Matcher matcherPhone = patternPhone.matcher(profileDTO.getPhoneNumber());
			if (matcherPhone.matches() == false) {
				Map<String, String> error = new HashMap<>();
				error.put("error", "Invalid phone number!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			if (!(profileDTO.getFirstName().trim().matches("^[\\p{IsHani}\\p{IsLatin}\\s]{1,50}$"))) {
				Map<String, String> error = new HashMap<>();
				error.put("error", "Invalid first name! Please enter more than one character.");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

			if(!(profileDTO.getLastName().trim().matches("^[\\p{IsHani}\\p{IsLatin}\\s]{1,50}$"))){
				Map<String, String> error = new HashMap<>();
				error.put("error", "Invalid last name! Please enter more than one character.");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			if (!(profileDTO.getAddress().trim().matches("^(?![-,])[\\p{IsHani}\\p{IsLatin}\\s,-]{5,100}$"))) {
				Map<String, String> error = new HashMap<>();
				error.put("error", "Invalid address! Please enter more than five characters.");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			if (findId.isEmpty()) {
				Map<String, String> error = new HashMap<>();
				error.put("error", "Account not found!");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}

			Profile profileToUpdate = findId.get();
			if (profileDTO.getAddress() != null) {
				profileToUpdate.setAddress(profileDTO.getAddress());
			}
			if (profileDTO.getFirstName() != null) {
				profileToUpdate.setFirstName(profileDTO.getFirstName());
			}
			if (profileDTO.getLastName() != null) {
				profileToUpdate.setLastName(profileDTO.getLastName());
			}
			Boolean isGender = profileDTO.isGender();
			profileToUpdate.setGender(profileDTO.isGender());
			if (profileDTO.getPhoneNumber() != null) {
				profileToUpdate.setPhoneNumber(profileDTO.getPhoneNumber());
			}
			// Check if avatar is provided
			if (profileDTO.getAvatar() != null && !profileDTO.getAvatar().isEmpty()) {
				profileToUpdate.setAvatar(profileDTO.getAvatar().getBytes());
			}
			// Check if CV is provided
			if (profileDTO.getCV() != null && !profileDTO.getCV().isEmpty()) {
				profileToUpdate.setCV(profileDTO.getCV().getBytes());
			}
			profileToUpdate.setAccount(account);
			Profile newProfile = profileRepo.save(profileToUpdate);
			Map<String, String> success = new HashMap<String, String>();
			success.put("success", "Profile updated successfully !!!");
			return new ResponseEntity<>(success, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

//	@Override
//	public ResponseEntity<?> viewProfile(ProfileDTO profileDTO) {
//
//		Optional<Account> findAccountByEmail = accountRepo.findFirstByEmail(profileDTO.getEmail());
//		Account account = findAccountByEmail.get();
//		Optional<Profile> findId = profileRepo.findFirstByAccount_id(account.getId());
//
//		if (findId.isEmpty()) {
//			Map<String, String> error = new HashMap<>();
//			error.put("error", "Account not found!");
//			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//		}
//		Profile profileEntity = account.getProfile();
//
//		if (profileEntity == null) {
//			Map<String, String> error = new HashMap<>();
//			error.put("error", "Profile not found!");
//			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//		}
//
//		ProfileDTO responseDTO = new ProfileDTO();
//		responseDTO.setFirstName(profileEntity.getFirstName());
//		responseDTO.setLastName(profileEntity.getLastName());
//		responseDTO.setPhoneNumber(profileEntity.getPhoneNumber());
//		responseDTO.setAddress(profileEntity.getAddress());
//		responseDTO.setGender(profileEntity.isGender());
////        responseDTO.setCV(new String(Base64.getEncoder().encode(profileEntity.getCV())));
////        responseDTO.setAvatar(Base64.getEncoder().encodeToString(profileEntity.getAvatar()));
//
//		return ResponseEntity.ok(responseDTO);
//	}

	@Override
	public ResponseEntity<?> viewProfileByEmail(AccountDTO accountDTO) {
		System.out.print(accountDTO.getId());
		Optional<Profile> findProfileByEmail = profileRepo.findFirstByAccount_id(accountDTO.getId());

		if (findProfileByEmail.isPresent()) {
			Map<String, Object> success = new HashMap<String, Object>();
			success.put("success", "Get profile successfully !!!");
			ProfileDTOWithImgCV responseDTO = new ProfileDTOWithImgCV();
			responseDTO.setFirstName(findProfileByEmail.get().getFirstName());
			responseDTO.setLastName(findProfileByEmail.get().getLastName());
			responseDTO.setPhoneNumber(findProfileByEmail.get().getPhoneNumber());
			responseDTO.setAddress(findProfileByEmail.get().getAddress());
			responseDTO.setGender(findProfileByEmail.get().isGender());
			responseDTO.setAvatar(findProfileByEmail.get().getAvatar());
			responseDTO.setCv(findProfileByEmail.get().getCV());
			success.put("metadata", responseDTO);
			return ResponseEntity.ok(success);
		}

		Map<String, String> error = new HashMap<>();
		error.put("error", "Profile not found!");
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> updateProfileCVByEmail(UpdateProfileDTO profileDTO) {
		String email = profileDTO.getEmail();
		if (email == null) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Email cannot be null");
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
		Optional<Account> account = accountRepo.findFirstByEmail(email);
		if (account.isEmpty()) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Account not found");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
		Optional<Profile> profileOptional = profileRepo.findFirstByAccount_id(account.get().getId());
		if (profileOptional.isEmpty()) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Profile not found");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
		Profile profileToUpdate = profileOptional.get();
		try {
			MultipartFile cvFile = profileDTO.getCV();
			if (cvFile != null && !cvFile.isEmpty()) {
				profileToUpdate.setCV(cvFile.getBytes());
			}
			profileRepo.save(profileToUpdate);

			Map<String, Object> success = new HashMap<>();
			success.put("success", "Profile updated successfully");
			return new ResponseEntity<>(success, HttpStatus.ACCEPTED);
		} catch (IOException e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Failed to update profile");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> updateProfileAvatarByEmail(UpdateProfileDTO profileDTO) {
		String email = profileDTO.getEmail();
		if (email == null) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Email cannot be null");
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
		Optional<Account> account = accountRepo.findFirstByEmail(email);
		if (account.isEmpty()) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Account not found");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
		Optional<Profile> profileOptional = profileRepo.findFirstByAccount_id(account.get().getId());
		if (profileOptional.isEmpty()) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Profile not found");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
		Profile profileToUpdate = profileOptional.get();
		try {

			MultipartFile avatarFile = profileDTO.getAvatar();
			if (avatarFile != null && !avatarFile.isEmpty()) {
				profileToUpdate.setAvatar(avatarFile.getBytes());
			}

			profileRepo.save(profileToUpdate);

			Map<String, Object> success = new HashMap<>();
			success.put("success", "Profile updated successfully");
			return new ResponseEntity<>(success, HttpStatus.ACCEPTED);
		}catch (IOException e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Failed to update profile");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
public ResponseEntity<?> getAllProfile() {
		List<Profile> findAll = profileRepo.findAll();
		List<ProfileDTO> dtos = new ArrayList<>();

		if (!findAll.isEmpty()) {

			for (Profile profile : findAll) {
				ProfileDTO profileDTO = new ProfileDTO();
				profileDTO.setFirstName(profile.getFirstName());
				profileDTO.setLastName(profile.getLastName());
				dtos.add(profileDTO);
			}

			Map<String, Object> success = new HashMap<String, Object>();
			success.put("success", "Get profiles successfully!");
			success.put("metadata", dtos);
			return ResponseEntity.ok(success);
		} else {
			Map<String, String> info = new HashMap<>();
			info.put("info", "No profiles found.");
			return ResponseEntity.ok(info);
		}
	}}

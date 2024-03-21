package com.swp.server.controller;

import com.swp.server.dto.AccountDTO;
import com.swp.server.dto.ProfileDTO;
import com.swp.server.dto.UpdateProfileDTO;
import com.swp.server.entities.Account;
import com.swp.server.entities.Profile;
import com.swp.server.repository.AccountRepo;
import com.swp.server.repository.ProfileRepo;
import com.swp.server.services.profile.ProfileService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/profile")
public class ProfileController {

	@Autowired
	private ProfileRepo profileRepo;

	@Autowired
	ProfileService profileService;

	@Autowired
	AccountRepo accountRepo;

	@PutMapping("/createprofile")
	public ResponseEntity<?> createProfile(@ModelAttribute ProfileDTO profileDTO) {
		return profileService.createProfile(profileDTO);
	}

	// view profile
	@PostMapping("/viewDetail")
	public ResponseEntity<?> viewProfileByEmail(@RequestBody AccountDTO account) {
		return profileService.viewProfileByEmail(account);
	}

	

	// update cv
	@PutMapping("/update-profile-cv")
	public ResponseEntity<?> updateProfileCVByEmail(@ModelAttribute UpdateProfileDTO profileDTO) {
		return profileService.updateProfileCVByEmail(profileDTO);
	}

	// update avatar
	@PutMapping("/update-profile-avatar")
	public ResponseEntity<?> updateProfileAvatarByEmail(@ModelAttribute UpdateProfileDTO profileDTO) {
		return profileService.updateProfileAvatarByEmail(profileDTO);
	}

	// dowload cv
	@GetMapping("/downloadFile/{email}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String email, HttpServletRequest request) {
		try {

			Optional<Account> account = accountRepo.findFirstByEmail(email);

			if (!account.isPresent()) {
				return ResponseEntity.badRequest().body(null);
			}

			Optional<Profile> profile = profileRepo.findFirstByAccount_id(account.get().getId());

			if (!profile.isPresent()) {
				// Handle case when profile is not found
				return ResponseEntity.notFound().build();
			}

			// Convert varbinary(MAX) to byte array
			byte[] fileBytes = profile.get().getCV();

			ByteArrayResource resource = new ByteArrayResource(fileBytes);

			// Set the content type specifically for PDF files
			String contentType = "application/pdf";

			String cleanedFileName = cleanHeaderField(profile.get().getStringCV());

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

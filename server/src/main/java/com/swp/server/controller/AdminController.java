package com.swp.server.controller;

import com.swp.server.dto.*;
import com.swp.server.services.admin.AdminService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
public class AdminController {

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private AdminService adminService;

	@GetMapping("/hello")
	public ResponseEntity<?> hello() {
		return ResponseEntity.ok("Hello world");
	}

	@PostMapping("/accounts")
	public ResponseEntity<?> accounts(@RequestParam(value = "page", required = false) Integer page,
			@RequestBody ViewListAccountDTO viewListAccountDTO) {
		return adminService.viewListAccounts(viewListAccountDTO, page);
	}

	@PostMapping("/createAccount")
	public ResponseEntity<?> createAccount(@RequestBody CreateAccountDTO createAccountDTO) {
		return adminService.createAccount(createAccountDTO);
	}

	@PostMapping("/blockAccount")
	public ResponseEntity<?> blockAccount(@RequestBody BlockAccountDTO blockAccountDTO) {
		return adminService.blockAccount(blockAccountDTO);
	}

	@PostMapping("/searchAccount")
	public ResponseEntity<?> searchAccount(@RequestBody SearchAccountDTO searchAccountDTO) {
		return adminService.searchAccount(searchAccountDTO);
	}

	@PostMapping("/countByRole")
	public ResponseEntity<?> countByRole() {
		return adminService.countByRole();
	}

}

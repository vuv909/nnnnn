package com.swp.server.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swp.server.dto.AccountDTO;
import com.swp.server.dto.ChangePasswordDTO;
import com.swp.server.dto.ChangePasswordLoginDTO;
import com.swp.server.dto.ChatDTOId;
import com.swp.server.dto.EmailDTO;
import com.swp.server.dto.LoginDTO;
import com.swp.server.dto.MessageDTO;
import com.swp.server.dto.OTPCodeAndEmailDTO;
import com.swp.server.dto.ReceiverOtpCode;
import com.swp.server.dto.SignUpDTO;
import com.swp.server.dto.UpdateUsernameDTO;
import com.swp.server.dto.UserChat;
import com.swp.server.entities.Account;
import com.swp.server.entities.Role;
import com.swp.server.repository.AccountRepo;
import com.swp.server.repository.RoleRepo;
import com.swp.server.services.auth.AuthService;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AccountRepo accountRepo;
	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpDTO signUpDTO) {
		return authService.createAccount(signUpDTO);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
		return authService.login(loginDTO);
	}

	@PostMapping("/changePasswordWhenLogin")
	public ResponseEntity<?> changePasswordWhenLogin(@RequestBody ChangePasswordLoginDTO changePasswordDTO) {
		return authService.changePasswordWhenLogin(changePasswordDTO);
	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
		return authService.changePassword(changePasswordDTO);
	}

	// send otp code
	@PostMapping("/sendOtpCode")
	public ResponseEntity<?> sendOtpCode(@RequestBody ReceiverOtpCode receiverOtpCode) {
		return authService.sendOtpCodeToUser(receiverOtpCode);
	}

	// verify account
	@PostMapping("/verifyAccount")
	public ResponseEntity<?> verifyAccount(@RequestBody OTPCodeAndEmailDTO codeAndEmailDTO) {
		return authService.verifyAccount(codeAndEmailDTO);
	}

	// find account by email
	@PostMapping("/findEmail")
	public ResponseEntity<?> findAccountByEmail(@RequestBody EmailDTO emailDTO) {
		return authService.findAccountByEmail(emailDTO);
	}

	// update username
	@PutMapping("/updateUsername")
	public ResponseEntity<?> updateUserName(@RequestBody UpdateUsernameDTO updateUsernameDTO) {
		return authService.updateUserName(updateUsernameDTO);
	}

	@GetMapping("/role/{email}")
	public ResponseEntity<?> getRole(@PathVariable String email) {
		return authService.getRole(email);
	}

}

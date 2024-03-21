package com.swp.server.services.auth;

import org.springframework.http.ResponseEntity;

import com.swp.server.dto.ChangePasswordDTO;
import com.swp.server.dto.ChangePasswordLoginDTO;
import com.swp.server.dto.EmailDTO;
import com.swp.server.dto.LoginDTO;
import com.swp.server.dto.OTPCodeAndEmailDTO;
import com.swp.server.dto.ReceiverOtpCode;
import com.swp.server.dto.SignUpDTO;
import com.swp.server.dto.UpdateUsernameDTO;
import com.swp.server.entities.Account;

public interface AuthService {
	public ResponseEntity<?> createAccount(SignUpDTO signUpDTO);

	public ResponseEntity<?> login(LoginDTO loginDTO);

	public ResponseEntity<?> changePassword(ChangePasswordDTO changePasswordDTO);

	public boolean sendOtpCode(Account accountF, Long otpVerificationMail);

	public ResponseEntity<?> sendOtpCodeToUser(ReceiverOtpCode receiverOtpCode);

	public ResponseEntity<?> verifyAccount(OTPCodeAndEmailDTO codeAndEmailDTO);

	public ResponseEntity<?> findAccountByEmail(EmailDTO emailDTO);

	public ResponseEntity<?> updateUserName(UpdateUsernameDTO updateUsernameDTO);

	public ResponseEntity<?> getRole(String email);

	public ResponseEntity<?> changePasswordWhenLogin(ChangePasswordLoginDTO changePasswordDTO);

}

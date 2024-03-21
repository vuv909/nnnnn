package com.swp.server.services.auth;

import java.io.UnsupportedEncodingException;
import java.lang.StackWalker.Option;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.swp.server.dto.AccountDTO;
import com.swp.server.dto.ChangePasswordDTO;
import com.swp.server.dto.ChangePasswordLoginDTO;
import com.swp.server.dto.CheckMailDTO;
import com.swp.server.dto.EmailDTO;
import com.swp.server.dto.LoginDTO;
import com.swp.server.dto.OTPCodeAndEmailDTO;
import com.swp.server.dto.ReceiverOtpCode;
import com.swp.server.dto.ResponseLogin;
import com.swp.server.dto.SignUpDTO;
import com.swp.server.dto.UpdateUsernameDTO;
import com.swp.server.entities.Account;
import com.swp.server.entities.Profile;
import com.swp.server.entities.Role;
import com.swp.server.enums.AccountRole;
import com.swp.server.repository.AccountRepo;
import com.swp.server.repository.ProfileRepo;
import com.swp.server.repository.RoleRepo;
import com.swp.server.services.jwt.UserDetailsServiceImpl;
import com.swp.server.utils.JwtUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private JavaMailSenderImpl javaMailSenderImp;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ProfileRepo profileRepo;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Override
	public ResponseEntity<?> changePasswordWhenLogin(ChangePasswordLoginDTO changePasswordDTO) {
		try {

			Optional<Account> optionalAccount = accountRepo.findFirstByEmail(changePasswordDTO.getEmail());
			if (optionalAccount.isEmpty()) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "NOT FOUND ACCOUNT !!!");
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}
			Account account = optionalAccount.get();
			String storedPassword = account.getPassword(); // Get the stored hashed password from the database
			String oldPassword = changePasswordDTO.getOldPassword();

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			if (!passwordEncoder.matches(oldPassword, storedPassword)) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "OLD PASSWORD DOES NOT MATCH !!!");
				return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
			}

			String passwordRegex = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
			Pattern patternPassword = Pattern.compile(passwordRegex);
			Matcher matcherPassword = patternPassword.matcher(changePasswordDTO.getPassword());
			if (matcherPassword.matches() == false) {
				Map<String, String> error = new HashMap<String, String>();
				error.put("error",
						"Password at least 8 characrter , 1 uppercase , 1 special character and should not contain any whitespace characters.  !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

			optionalAccount.get().setPassword(new BCryptPasswordEncoder().encode(changePasswordDTO.getPassword()));
			accountRepo.save(optionalAccount.get());
			Map<String, String> success = new HashMap<String, String>();
			success.put("success", "Change password successfully !!!");
			return new ResponseEntity<>(success, HttpStatus.OK);

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> getRole(String email) {

		try {

			Optional<Account> optionalAccount = accountRepo.findFirstByEmail(email);

			if (optionalAccount.isPresent()) {

				Map<String, String> success = new HashMap<String, String>();
				String role = optionalAccount.get().getRole().getName();
				success.put("role", role);
				return new ResponseEntity<>(success, HttpStatus.OK);

			} else {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Account not exist !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// sign up
	@Override
	public ResponseEntity<?> createAccount(SignUpDTO signUpDTO) {

		System.out.println(signUpDTO.getEmail() + " " + signUpDTO.getPassword() + " " + signUpDTO.getUsername());

		try {

			CheckMailDTO response = restTemplate.getForObject(
					"https://emailverification.whoisxmlapi.com/api/v3?apiKey=at_diEFn6Z2zEs4QNnPquwGEYcUKDm3D&emailAddress="
							+ signUpDTO.getEmail(),
					CheckMailDTO.class);

			if (response != null && response.isSmtpCheck()) {

				Optional<Account> checkUsernameExist = accountRepo.findFirstByUsername(signUpDTO.getUsername());
				if (checkUsernameExist.isPresent()) {
					Map<String, String> error = new HashMap<String, String>();
					error.put("error", "Username is existed  !!!");
					return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
				}

				Optional<Account> checkEmailExist = accountRepo.findFirstByEmail(signUpDTO.getEmail());
				if (checkEmailExist.isPresent()) {
					Map<String, String> error = new HashMap<String, String>();
					error.put("error", "Email is existed  !!!");
					return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
				}

				String passwordRegex = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
				Pattern patternPassword = Pattern.compile(passwordRegex);
				Matcher matcherPassword = patternPassword.matcher(signUpDTO.getPassword());
				if (matcherPassword.matches() == false) {
					Map<String, String> error = new HashMap<String, String>();
					error.put("error",
							"Password at least 8 characrter , 1 uppercase , 1 special character and should not contain any whitespace characters.  !!!");
					return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
				}

				Role roleEntity = roleRepo.findByName("JOBSEEKER");
				if (roleEntity == null) {
					Map<String, String> error = new HashMap<String, String>();
					error.put("error", "Role is empty !!!");
					return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
				}

				Profile profile = new Profile();
				Account newAcountEntity = new Account();
				newAcountEntity.setUsername(signUpDTO.getUsername());
				newAcountEntity.setEmail(signUpDTO.getEmail());
				newAcountEntity.setPassword(new BCryptPasswordEncoder().encode(signUpDTO.getPassword()));
				newAcountEntity.setVerify(false);
				newAcountEntity.setEnabled(true);
				newAcountEntity.setRole(roleEntity);

				profile.setAccount(newAcountEntity);

				Account newAccount = accountRepo.save(newAcountEntity);
				Profile newProfile = profileRepo.save(profile);

				Map<String, String> success = new HashMap<String, String>();
				success.put("success", "Sign up successfully !!!");
				return new ResponseEntity<>(success, HttpStatus.OK);
			} else {
				Map<String, String> error = new HashMap<String, String>();
				error.put("error", "Email is not exist !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// login
	@Override
	public ResponseEntity<?> login(LoginDTO loginDTO) {
		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
		} catch (BadCredentialsException e) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("error", "Incorrect username or password");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
		} catch (DisabledException disabledException) {

			Map<String, String> error = new HashMap<String, String>();
			error.put("error", "User not active");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
		}

		final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginDTO.getEmail());
		Optional<Account> optionalUser = accountRepo.findFirstByEmail(userDetails.getUsername());

		if (optionalUser.isPresent()) {

			if (optionalUser.get().isEnabled() == false) {
				Map<String, String> error = new HashMap<String, String>();
				error.put("error", "Your account is blocked !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

			final String jwt = jwtUtil.generateToken(optionalUser.get().getEmail(), loginDTO.isRemember());
			ResponseLogin responseLogin = new ResponseLogin();
			responseLogin.setId(optionalUser.get().getId());
			responseLogin.setEmail(optionalUser.get().getEmail());
			responseLogin.setJwt(jwt);
			responseLogin.setEnable(optionalUser.get().isEnabled());

			return ResponseEntity.ok(responseLogin);
		} else {
			Map<String, String> error = new HashMap<String, String>();
			error.put("error", "Your account is blocked !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<?> changePassword(ChangePasswordDTO changePasswordDTO) {
		String passwordRegex = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
		Pattern patternPassword = Pattern.compile(passwordRegex);
		Matcher matcherPassword = patternPassword.matcher(changePasswordDTO.getPassword());
		if (matcherPassword.matches() == false) {
			Map<String, String> error = new HashMap<String, String>();
			error.put("error",
					"Password at least 8 characrter , 1 uppercase , 1 special character and should not contain any whitespace characters.  !!!");
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}

		Optional<Account> account = accountRepo.findFirstByEmail(changePasswordDTO.getEmail());
		if (account.isPresent()) {
			account.get().setPassword(new BCryptPasswordEncoder().encode(changePasswordDTO.getPassword()));
			Account changePassword = accountRepo.save(account.get());

			if (changePassword == null) {
				Map<String, String> error = new HashMap<String, String>();
				error.put("error", "Error when change password !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

			Map<String, String> success = new HashMap<String, String>();
			success.put("success", "Change password successfully !!!");
			return new ResponseEntity<>(success, HttpStatus.ACCEPTED);

		} else {
			Map<String, String> error = new HashMap<String, String>();
			error.put("error", "Email not exist !!!");
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}

	}

	// check valid otp
	public boolean isOtpCodeValid(String otpCode, String email) {
		Optional<Account> account = accountRepo.findFirstByEmailAndOtpCode(email, otpCode);
		if (account.isPresent()) {
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.MINUTE, -2);

				Date nowPlus5Minutes = calendar.getTime();

				Long otpLong = Long.parseLong(account.get().getOtpCode());
				Date otpCreatedDate = account.get().getTimeOtpCreated();

				if (otpCreatedDate.before(nowPlus5Minutes)) {
					return false;
				}

				return true;
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}

	// send email
	@Override
	public boolean sendOtpCode(Account account, Long otpVerificationMail) {
		try {
			String subject = "OTP code";
			String senderName = "admin";
			String mailContent = "<p>Dear " + account.getEmail() + ",</p>";
			mailContent += "<h3>This is your OTP code : " + otpVerificationMail + "</h3>";
			mailContent += "<p>Otp code will expire in 5 minutes !</p>";
			mailContent += "<p>Thank you</p>";

			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom("thaimih834@gmail.com", senderName);
			helper.setTo(account.getEmail());
			helper.setSubject(subject);
			helper.setText(mailContent, true);

			Properties props = javaMailSenderImp.getJavaMailProperties();
			props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

			javaMailSender.send(message);
			return true;
		} catch (MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}

	// send otpcode api service
	@Override
	public ResponseEntity<?> sendOtpCodeToUser(ReceiverOtpCode receiverOtpCode) {

		Optional<Account> user = accountRepo.findFirstByEmail(receiverOtpCode.getEmail());
		String otpString = ThreadLocalRandom.current().nextLong(100000L, 1000000L) + "";
		Long otpVerificationMail = Long.parseLong(otpString);

		if (user.isPresent()) {
			boolean send = sendOtpCode(user.get(), otpVerificationMail);
			String optCode = String.valueOf(otpVerificationMail);
			if (send == true) {
				user.get().setOtpCode(optCode);
				user.get().setTimeOtpCreated(new Date());
				Account update = accountRepo.save(user.get());
				Map<String, Object> success = new HashMap<>();
				success.put("success", "Otp send successfully !!!");
				return ResponseEntity.ok(success);
			} else {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Error when send otp code !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
		}
		Map<String, Object> error = new HashMap<>();
		error.put("error", "Can find user !!!");
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> verifyAccount(OTPCodeAndEmailDTO codeAndEmailDTO) {

		boolean isOTPCodeValid = isOtpCodeValid(codeAndEmailDTO.getOtpCode(), codeAndEmailDTO.getEmail());

		if (isOTPCodeValid == true) {
			Optional<Account> account = accountRepo.findFirstByEmailAndOtpCode(codeAndEmailDTO.getEmail(),
					codeAndEmailDTO.getOtpCode());
			if (account.isPresent()) {
				account.get().setVerify(true);
				Account saveAccount = accountRepo.save(account.get());
				if (saveAccount != null) {
					Map<String, Object> success = new HashMap<>();
					success.put("success", "Verify account successfully !!!");
					return ResponseEntity.ok(success);
				}
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Error when verify account !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
			Map<String, Object> error = new HashMap<>();
			error.put("error", "Account not exist !!!");
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}

		Map<String, Object> error = new HashMap<>();
		error.put("error", "OTP code is not valid or expire !!!");
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> findAccountByEmail(EmailDTO emailDTO) {

		Optional<Account> account = accountRepo.findFirstByEmail(emailDTO.getEmail());
		if (account.isPresent()) {
			AccountDTO accountDTO = new AccountDTO();
			accountDTO.setId(account.get().getId());
			accountDTO.setVerified(account.get().isVerify());
			accountDTO.setUsername(account.get().getUsername());
			Map<String, Object> success = new HashMap<>();
			success.put("success", "Get account successfully !!!");
			success.put("metadata", accountDTO);
			return ResponseEntity.ok(success);

		}
		Map<String, Object> error = new HashMap<>();
		error.put("error", "Not found account !!!");
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

	}

	@Override
	public ResponseEntity<?> updateUserName(UpdateUsernameDTO updateUsernameDTO) {
		Optional<Account> account = accountRepo.findFirstByEmail(updateUsernameDTO.getEmail());
		if (account.isPresent()) {
			account.get().setUsername(updateUsernameDTO.getUsername());
			Account updateAccount = accountRepo.save(account.get());
			if (updateAccount != null) {
				Map<String, Object> success = new HashMap<>();
				success.put("success", "Update account successfully !!!");
				return ResponseEntity.ok(success);
			} else {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Error when updated account !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}
		}
		Map<String, Object> error = new HashMap<>();
		error.put("error", "Not found account !!!");
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

	}

}

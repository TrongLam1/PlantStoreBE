package com.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.dto.MailDTO;
import com.java.dto.UserDTO;
import com.java.dto.authentication.AuthenticationRequest;
import com.java.dto.authentication.JwtAuthenticationResponse;
import com.java.dto.authentication.RefreshTokenRequest;
import com.java.dto.authentication.SignUpRequest;
import com.java.entities.UserEntity;
import com.java.repositories.UserRepository;
import com.java.service.impl.AuthenticationServiceImpl;
import com.java.service.impl.JwtServiceImpl;
import com.java.service.impl.MailService;
import com.java.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController {

	private final AuthenticationServiceImpl authenticationService;
	private final UserServiceImpl userService;
	private final JwtServiceImpl jwtService;
	
	@Autowired
	private MailService mailService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequest request) throws RuntimeException {
		return ResponseEntity.ok(authenticationService.signup(request));
	}

	@PostMapping("/signin")
	public ResponseEntity<?> signin(@RequestBody AuthenticationRequest request) throws RuntimeException {
		ResponseEntity<?> response = authenticationService.signin(request);
		if (response == null) {
			// Authentication failed, return a 401 Unauthorized response
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		} else {
			// Authentication successful, return a 200 OK response
			return ResponseEntity.ok(response);
		}
	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest request) {
		return ResponseEntity.ok(authenticationService.refreshToken(request));
	}

	@GetMapping("/find")
	public ResponseEntity<?> findUser(@RequestHeader("Authorization") String jwtToken) {
		String token = jwtToken.substring(7);
		String userEmail = jwtService.extractUsername(token);
		return ResponseEntity.ok(userService.findByEmail(userEmail));
	}

	@PutMapping("/update-info")
	public ResponseEntity<?> updateInfo(@RequestHeader("Authorization") String jwtToken,@RequestBody SignUpRequest request) {
		String token = jwtToken.substring(7);
		String userEmail = jwtService.extractUsername(token);
		return ResponseEntity.ok(userService.updateUser(userEmail, request));
	}
	
	@PostMapping("/sendOtp/{mail}")
	public String sendMail(@PathVariable String mail) {
		authenticationService.generateOtp(mail);
		UserDTO user = userService.findByEmail(mail);
		String otp = user.getOtp();
		MailDTO mailDTO = new MailDTO();
		mailDTO.setSubject("Otp Reset Password");
		mailDTO.setMessage("Otp: " + otp);
		mailService.sendMail(mail, mailDTO);
		return "Successfully to send the mail!";
	}
	
	@PostMapping("/reset/{mail}/{otp}")
	public String resetPassword(@PathVariable String mail, @PathVariable String otp, @RequestParam String newPass) {
		return authenticationService.resetPassword(otp, mail, newPass);
	}
}

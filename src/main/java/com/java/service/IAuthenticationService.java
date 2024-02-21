package com.java.service;

import org.springframework.http.ResponseEntity;

import com.java.dto.authentication.AuthenticationRequest;
import com.java.dto.authentication.JwtAuthenticationResponse;
import com.java.dto.authentication.RefreshTokenRequest;
import com.java.dto.authentication.SignUpRequest;


public interface IAuthenticationService {

	ResponseEntity<?> signup(SignUpRequest request);
	ResponseEntity<?> signin(AuthenticationRequest request);
	JwtAuthenticationResponse refreshToken(RefreshTokenRequest request);
	String generateOtp(String email);
	String resetPassword(String otp, String email, String newPass);
}

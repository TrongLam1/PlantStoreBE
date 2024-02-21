package com.java.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.java.dto.UserDTO;
import com.java.dto.authentication.SignUpRequest;

public interface IUserService {

	ResponseEntity<?> getUsers(int pageNo, int pageSize);
	UserDetailsService userDetailsService();
	UserDTO findByEmail(String email);
	ResponseEntity<?> updateUser(String email, SignUpRequest request);
}

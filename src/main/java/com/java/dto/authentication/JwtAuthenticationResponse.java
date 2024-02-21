package com.java.dto.authentication;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

	private String token;
	private String refreshToken;
	private String name;
	private String email;
	private String role;
}

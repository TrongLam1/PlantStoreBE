package com.java.dto.authentication;

import com.java.entities.Role;

import lombok.Data;

@Data
public class SignUpRequest {

	private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
}

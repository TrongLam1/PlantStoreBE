package com.java.dto;

import java.util.Date;

import com.java.entities.Role;

import lombok.Data;

@Data
public class UserDTO {

	private Long id;
	private Date createdDate;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Role role;
    private String otp;
}

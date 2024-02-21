package com.java.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

	private Long id;
	
	private Date createdDate;
	
    private String content;
    
    private int rating;
    
    private String username;
}

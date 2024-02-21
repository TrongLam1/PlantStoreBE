package com.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.dto.request.CommentRequest;
import com.java.service.impl.CommentServiceImpl;
import com.java.service.impl.JwtServiceImpl;

@RestController
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	private JwtServiceImpl jwtService;

	@Autowired
	private CommentServiceImpl commentService;

	@PostMapping("/post-comment/{id}")
	public ResponseEntity<?> postComment(@RequestHeader("Authorization") String jwtToken,
			@RequestBody CommentRequest request, @PathVariable("id") Long plantId) {
		String token = jwtToken.substring(7);
		String userEmail = jwtService.extractUsername(token);
		return commentService.saveComment(request, userEmail, plantId);
	}
	
	@GetMapping("/get-all-comments-for-plant/{id}")
	public ResponseEntity<?> getAllCommentsForPlant(@PathVariable("id") Long plantId) {
		return commentService.getAllCommentsForPlant(plantId);
	}
}

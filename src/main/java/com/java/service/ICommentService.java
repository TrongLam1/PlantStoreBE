package com.java.service;

import org.springframework.http.ResponseEntity;

import com.java.dto.request.CommentRequest;

public interface ICommentService {

	ResponseEntity<?> saveComment(CommentRequest request, String email, Long plantId);
	ResponseEntity<?> getAllCommentsForPlant(Long plantId);
	Double getAverageRatingForPlant(Long plantId);
}

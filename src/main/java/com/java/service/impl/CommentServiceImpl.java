package com.java.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.java.converter.CommentConverter;
import com.java.dto.CommentDTO;
import com.java.dto.request.CommentRequest;
import com.java.entities.CommentEntity;
import com.java.entities.PlantEntity;
import com.java.entities.UserEntity;
import com.java.repositories.CommentRepository;
import com.java.repositories.PlantRepository;
import com.java.repositories.UserRepository;
import com.java.service.ICommentService;

@Service
public class CommentServiceImpl implements ICommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private PlantRepository plantRepository;

	@Override
	public ResponseEntity<?> saveComment(CommentRequest request, String email, Long plantId) {
		try {
			UserEntity user = userRepository.findByEmail(email).get();
			PlantEntity plant = plantRepository.findById(plantId).get();
			
			CommentEntity comment = new CommentEntity();
			comment.setContent(request.getContent());
			comment.setRating(request.getRating());
			comment.setUser(user);
			comment.setPlant(plant);
			
			commentRepository.save(comment);
			
			double rating = commentRepository.getAverageRatingForPlant(plantId);
			plant.setRating(rating);
			plantRepository.save(plant);
			
			return ResponseEntity.ok("Đã đánh giá sản phẩm.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e);
		}
	}

	@Override
	public ResponseEntity<?> getAllCommentsForPlant(Long plantId) {
		try {
			List<CommentEntity> listResults = commentRepository.findByPlantId(plantId);
			List<CommentDTO> listResultsDTO = new ArrayList<>();
			for (CommentEntity entity : listResults) {
				CommentDTO dto = CommentConverter.toDTO(entity);
				listResultsDTO.add(dto);
			}
			
			return ResponseEntity.ok(listResultsDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e);
		}
	}

	@Override
	public Double getAverageRatingForPlant(Long plantId) {
		double rating = commentRepository.getAverageRatingForPlant(plantId);
		PlantEntity plant = plantRepository.findById(plantId).get();
		plant.setRating(rating);
		
		return rating;
	}
}

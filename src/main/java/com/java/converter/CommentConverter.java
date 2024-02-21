package com.java.converter;

import org.springframework.stereotype.Component;

import com.java.dto.CommentDTO;
import com.java.entities.CommentEntity;

@Component
public class CommentConverter {

	public static CommentDTO toDTO(CommentEntity entity) {
		CommentDTO dto = new CommentDTO();
		dto.setId(entity.getId());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setContent(entity.getContent());
		dto.setRating(entity.getRating());
		dto.setUsername(entity.getUser().getName());
		
		return dto;
	}
}

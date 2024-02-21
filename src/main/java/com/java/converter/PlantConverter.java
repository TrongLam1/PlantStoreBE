package com.java.converter;

import org.springframework.stereotype.Component;

import com.java.dto.PlantDTO;
import com.java.entities.PlantEntity;


@Component
public class PlantConverter {

	public static PlantEntity toEntity(PlantDTO dto) {
		PlantEntity entity = new PlantEntity();
		entity.setCreatedDate(dto.getCreatedDate());
		entity.setName(dto.getName());
		entity.setType(dto.getType());
		entity.setDescription(dto.getDescription());
		entity.setOldPrice(dto.getOldPrice());
		entity.setSale(dto.getSale());
		entity.setNewPrice(dto.getNewPrice());
		entity.setRating(dto.getRating());
		entity.setImageUrl(dto.getImageUrl());
		entity.setImageId(dto.getImageId());
		entity.setValid(dto.getValid());
		
		return entity;
	}
	
	public static PlantDTO toDTO(PlantEntity entity) {
		PlantDTO dto = new PlantDTO();
		if (entity.getId() != null) {
			dto.setId(entity.getId());
		}
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setType(entity.getType());
		dto.setOldPrice(entity.getOldPrice());
		dto.setSale(entity.getSale());
		dto.setNewPrice(entity.getNewPrice());
		dto.setRating(entity.getRating());
		dto.setImageUrl(entity.getImageUrl());
		dto.setImageId(entity.getImageId());
		dto.setValid(entity.getValid());
		
		return dto;
	}
	
	public static PlantEntity toEntity(PlantDTO dto, PlantEntity entity) {
		entity.setCreatedDate(dto.getCreatedDate());
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setType(dto.getType());
		entity.setOldPrice(dto.getOldPrice());
		entity.setSale(dto.getSale());
		entity.setNewPrice(dto.getNewPrice());
		entity.setRating(dto.getRating());
		entity.setImageUrl(dto.getImageUrl());
		entity.setImageId(dto.getImageId());
		entity.setValid(dto.getValid());
		
		return entity;
	}
}

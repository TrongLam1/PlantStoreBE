package com.java.converter;

import org.springframework.stereotype.Component;

import com.java.dto.CartItemDTO;
import com.java.entities.CartItemEntity;

@Component
public class CartItemConverter {
	
	public static CartItemDTO toDTO(CartItemEntity entity) {
		CartItemDTO dto = new CartItemDTO();
		dto.setId(entity.getId());
		dto.setQuantity(entity.getQuantity());
		dto.setTotalPrice(entity.getTotalPrice());
		dto.setPlant(PlantConverter.toDTO(entity.getPlant()));
		
		return dto;
	}
}

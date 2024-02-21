package com.java.converter;

import org.springframework.stereotype.Component;

import com.java.dto.OrderItemDTO;
import com.java.entities.OrderItemEntity;

@Component
public class OrderItemConverter {

	public static OrderItemDTO toDTO(OrderItemEntity entity) {
		OrderItemDTO dto = new OrderItemDTO();
		dto.setId(entity.getId());
		dto.setQuantity(entity.getQuantity());
		dto.setTotalPrice(entity.getTotalPrice());
		dto.setPlant(PlantConverter.toDTO(entity.getPlant()));
		
//		OrderDTO orderDTO = OrderConverter.toDTO(entity.getOrder());
//		dto.setOrder(orderDTO);
		
		return dto;
	}
}

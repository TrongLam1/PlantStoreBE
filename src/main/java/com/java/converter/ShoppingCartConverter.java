package com.java.converter;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.java.dto.CartItemDTO;
import com.java.dto.ShoppingCartDTO;
import com.java.entities.CartItemEntity;
import com.java.entities.ShoppingCartEntity;

@Component
public class ShoppingCartConverter {

	public static ShoppingCartDTO toDTO(ShoppingCartEntity entity) {
		ShoppingCartDTO dto = new ShoppingCartDTO();
		dto.setId(entity.getId());
		dto.setTotalItems(entity.getTotalItems());
		dto.setTotalPrices(entity.getTotalPrice());
		
		Set<CartItemDTO> cartItemsDTO = new HashSet<>();
		for(CartItemEntity cartItem : entity.getCartItems()) {
			CartItemDTO cartDTO = CartItemConverter.toDTO(cartItem);
			cartItemsDTO.add(cartDTO);
		}
		dto.setCartItemsDTO(cartItemsDTO);
		
		return dto;
	}
}

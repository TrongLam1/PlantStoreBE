package com.java.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDTO {
	
	private Long id;
	
	private double totalPrices;
	
	private int totalItems;
	
	private Set<CartItemDTO> cartItemsDTO = new HashSet<>();
}

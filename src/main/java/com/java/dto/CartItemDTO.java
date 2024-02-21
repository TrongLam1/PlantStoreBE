package com.java.dto;

import lombok.Data;

@Data
public class CartItemDTO {

	private Long id;
	
	private ShoppingCartDTO cart;
	
	private PlantDTO plant;
	
	private int quantity;
	
	private double totalPrice;
}

package com.java.service;

import org.springframework.http.ResponseEntity;

public interface IShoppingCartService {

	ResponseEntity<?> addPlantToCart(String email, Long plantId, int quantity);
	ResponseEntity<?> getCartItemsForUser(String userEmail);
	ResponseEntity<?> findShoppingCartByUser(String email);
	ResponseEntity<?> removeItemFromShoppingCart(String email, Long plantId);
}

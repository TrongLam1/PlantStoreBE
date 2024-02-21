package com.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.service.impl.JwtServiceImpl;
import com.java.service.impl.ShoppingCartServiceImpl;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartServiceImpl shoppingCartService;

	@Autowired
	private JwtServiceImpl jwtService;

	@PostMapping("/add-plant")
	public ResponseEntity<?> addPlantToCart(
			@RequestParam String email,
			@RequestParam Long plantId,
			@RequestParam int quantity) {
		return shoppingCartService.addPlantToCart(email, plantId, quantity);
	}

	@GetMapping("/get-cart-item")
	public ResponseEntity<?> getCartItemsForUser(@RequestHeader("Authorization") String jwtToken) {
		String token = jwtToken.substring(7);
		String userEmail = jwtService.extractUsername(token);
		return shoppingCartService.getCartItemsForUser(userEmail);
	}
	
	@GetMapping("/get-shopping-cart")
	public ResponseEntity<?> getShoppingCartByUser(@RequestHeader("Authorization") String jwtToken) {
		String token = jwtToken.substring(7);
		String userEmail = jwtService.extractUsername(token);
		return ResponseEntity.ok(shoppingCartService.findShoppingCartByUser(userEmail));
	}

	@DeleteMapping("/remove/{id}")
	public ResponseEntity<?> removePlantFromCart(@RequestHeader("Authorization") String jwtToken,
			@PathVariable("id") Long plantId) {
		String token = jwtToken.substring(7);
		String userEmail = jwtService.extractUsername(token);
		return shoppingCartService.removeItemFromShoppingCart(userEmail, plantId);
	}
}

package com.java.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.converter.CartItemConverter;
import com.java.converter.ShoppingCartConverter;
import com.java.dto.CartItemDTO;
import com.java.entities.CartItemEntity;
import com.java.entities.PlantEntity;
import com.java.entities.ShoppingCartEntity;
import com.java.entities.UserEntity;
import com.java.repositories.CartItemRepository;
import com.java.repositories.PlantRepository;
import com.java.repositories.ShoppingCartRepository;
import com.java.repositories.UserRepository;
import com.java.service.IShoppingCartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements IShoppingCartService {
	
	private final ShoppingCartRepository shoppingCartRepository;
	private final CartItemRepository cartItemRepository;
	private final UserRepository userRepository;
	private final PlantRepository plantRepository;
	
	@Override
	public ResponseEntity<?> addPlantToCart(String email, Long plantId, int quantity) {
		try {
			UserEntity user = userRepository.findByEmail(email).get();
	        if (user.getCart() == null) {
	            ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
	            shoppingCart.setUser(user);
	            shoppingCart.setTotalItems(0);
	            shoppingCart.setTotalPrice(0.0);
	            shoppingCart.setCartItems(new HashSet<>());
	            user.setCart(shoppingCart);
	        }

	        PlantEntity plant = plantRepository.findById(plantId).get();
	        Optional<CartItemEntity> existingCartItem = user.getCart().getCartItems()
	                .stream()
	                .filter(cartItem -> cartItem.getPlant().getId().equals(plantId))
	                .findFirst();

	        if (existingCartItem.isPresent()) {
	            CartItemEntity cartItem = existingCartItem.get();
	            cartItem.setQuantity(cartItem.getQuantity() + quantity);
	            cartItem.setTotalPrice(plant.getNewPrice() * cartItem.getQuantity());
	        } else {
	            CartItemEntity newCartItem = new CartItemEntity();
	            newCartItem.setPlant(plant);
	            newCartItem.setQuantity(quantity);
	            double totalPrice = plant.getNewPrice() * newCartItem.getQuantity();
	            newCartItem.setTotalPrice(totalPrice);
	            newCartItem.setShoppingCart(user.getCart());
	            user.getCart().getCartItems().add(newCartItem);
	        }

	        updateCartTotal(user.getCart());

	        shoppingCartRepository.save(user.getCart());
	        
	        return ResponseEntity.ok("Đã thêm sản phẩm vào giỏ hàng.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thêm sản phẩm thất bại.");
		}
    }
	
	private void updateCartTotal(ShoppingCartEntity cart) {
        int totalItems = 0;
        double totalPrice = 0.0;

        for (CartItemEntity cartItem : cart.getCartItems()) {
            totalItems += cartItem.getQuantity();
            totalPrice += cartItem.getTotalPrice();
        }

        cart.setTotalItems(totalItems);
        cart.setTotalPrice(totalPrice);
    }
	
	@Override
	public ResponseEntity<?> getCartItemsForUser(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail).get();
        if (user.getCart() == null) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy giỏ hàng.");
        } else {
        	Set<CartItemEntity> cartItemsEntity = new HashSet<>(user.getCart().getCartItems());
        	Set<CartItemDTO> cartItemsDTO = new HashSet<>();
        	for (CartItemEntity cartItem : cartItemsEntity) {
        		CartItemDTO cartDTO = CartItemConverter.toDTO(cartItem);
        		cartItemsDTO.add(cartDTO);
        	}
        	return ResponseEntity.ok(cartItemsDTO);
        }
    }

	@Override
	public ResponseEntity<?> removeItemFromShoppingCart(String email, Long plantId) {
		try {
			UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		    ShoppingCartEntity shoppingCart = user.getCart();

		    Set<CartItemEntity> cartItems = shoppingCart.getCartItems();

		    // Identify the cart item to remove
		    CartItemEntity cartItemToRemove = null;
		    for (CartItemEntity cartItem : cartItems) {
		        if (cartItem.getPlant().getId().equals(plantId)) {
		            cartItemToRemove = cartItem;
		            break;
		        }
		    }

		    if (cartItemToRemove != null) {
		        cartItems.remove(cartItemToRemove);
		        cartItemToRemove.setShoppingCart(null); // Break the bidirectional link
		        cartItemRepository.delete(cartItemToRemove);
		        // Optionally, update the shopping cart in the database
		        shoppingCartRepository.save(shoppingCart);
		    }
		    
		    updateCartTotal(shoppingCart);
		    return ResponseEntity.ok("Đã xóa sản phẩm.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa sản phẩm thất bại.");
		}
	}

	@Override
	public ResponseEntity<?> findShoppingCartByUser(String email) {
		UserEntity user = userRepository.findByEmail(email).get();
		ShoppingCartEntity shoppingCart = shoppingCartRepository.findByUser(user);
		if (shoppingCart != null) {
			return ResponseEntity.ok(ShoppingCartConverter.toDTO(shoppingCart));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy giỏ hàng.");
		}
	}
}

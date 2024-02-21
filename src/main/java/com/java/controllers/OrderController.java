package com.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.dto.request.PlaceOrderRequest;
import com.java.service.impl.JwtServiceImpl;
import com.java.service.impl.OrderServiceImpl;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderServiceImpl orderService;

	@Autowired
	private JwtServiceImpl jwtService;

	@PostMapping("/create")
	public ResponseEntity<?> placeOrder(@RequestHeader("Authorization") String jwtToken,
			@RequestBody PlaceOrderRequest request) {
		try {
			String token = jwtToken.substring(7);
			String userEmail = jwtService.extractUsername(token);
			orderService.orderCreate(request, userEmail);
			return ResponseEntity.ok("Đặt hàng thành công.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đặt hàng không thành công.");
		}
	}

	@GetMapping("/getAllOrder")
	public ResponseEntity<?> findAllOrdersFromUser(@RequestHeader("Authorization") String jwtToken) {
		try {
			String token = jwtToken.substring(7);
			String email = jwtService.extractUsername(token);
			return orderService.getAllOrderFromUser(email);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found!");
		}
	}

	@GetMapping("/findByUUID/{uuid}")
	public ResponseEntity<?> findOrderByUUID(@PathVariable("uuid") String uuid) {
		return orderService.findOrderByUUID(uuid);
	}

	@PutMapping("/updateOrderStatus/{id}/{status}")
	public ResponseEntity<?> updateOrderStatus(@RequestHeader("Authorization") String jwtToken,
			@PathVariable("id") Long orderId,@PathVariable("status") int status) {
		return orderService.updateOrderStatus(orderId, status);
	}
	
	@GetMapping("/getOneOrder/{id}")
	public ResponseEntity<?> getOneOrder(@RequestHeader("Authorization") String jwtToken,
			@PathVariable("id") Long orderId) {
		String token = jwtToken.substring(7);
		String email = jwtService.extractUsername(token);
		return orderService.getOneOrder(email, orderId);
	}
}

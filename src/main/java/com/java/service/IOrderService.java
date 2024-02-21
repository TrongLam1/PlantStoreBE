package com.java.service;

import java.util.Date;

import org.springframework.http.ResponseEntity;

import com.java.dto.OrderStatusSummary;
import com.java.dto.request.PlaceOrderRequest;
import com.java.entities.OrderEntity;

public interface IOrderService {

	OrderEntity orderCreate(PlaceOrderRequest request, String email);
	ResponseEntity<?> findOrderByUUID(String trackingUUID);
	ResponseEntity<?> getAllOrderFromUser(String email);
	ResponseEntity<?> getAllOrders(int pageNo, int pageSize);
	ResponseEntity<?> updateOrderStatus(Long orderId, int status);
	ResponseEntity<?> getTotalPriceInDateRange(Date startDate, Date endDate);
	ResponseEntity<?> getTotalPriceByMonthInYear(int year);
	ResponseEntity<?> getOneOrder(String email, Long orderId);
	ResponseEntity<?> findOrderById(Long orderId);
	OrderStatusSummary countOrdersByStatus();
}

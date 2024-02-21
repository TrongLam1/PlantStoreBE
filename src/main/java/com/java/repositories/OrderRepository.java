package com.java.repositories;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.dto.OrderStatusSummary;
import com.java.entities.OrderEntity;
import com.java.entities.OrderStatus;
import com.java.entities.UserEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

	OrderEntity findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);

	@Query("SELECT o FROM OrderEntity o WHERE o.trackingUUID = ?1")
	OrderEntity findByTrackingUUID(UUID trackingUUID);

	List<OrderEntity> findByUser(UserEntity user);

	int countByUser(UserEntity user);

	@Query(value = "SELECT MONTH(created_date) as month, SUM(total_prices_order) as total\r\n"
			+ "FROM plantstore.orders\r\n" + "WHERE YEAR(created_date) = :year\r\n AND order_status = '1'"
			+ "GROUP BY MONTH(created_date)" + "ORDER BY MONTH(created_date);", nativeQuery = true)
	List<Object[]> getTotalPriceByMonthInYear(int year);

	@Query(value = "SELECT DATE_FORMAT(created_date, '%Y-%m-%d') as date, SUM(total_prices_order) as total "
			+ "FROM plantstore.orders " + "WHERE created_date BETWEEN :startDate AND :endDate AND order_status = '1'"
			+ "GROUP BY DATE_FORMAT(created_date, '%Y-%m-%d')" + "ORDER BY created_date;", nativeQuery = true)
	List<Object[]> getTotalPriceInDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query(value = "SELECT COUNT(o) FROM OrderEntity o WHERE o.orderStatus = '0'")
    long countPendingOrders();

    @Query(value = "SELECT COUNT(o) FROM OrderEntity o WHERE o.orderStatus = '1'")
    long countDeliveredOrders();

    @Query(value = "SELECT COUNT(o) FROM OrderEntity o WHERE o.orderStatus = '2'")
    long countCanceledOrders();

    @Query(value = "SELECT SUM(CASE WHEN o.orderStatus = '1' THEN o.totalPricesOrder ELSE 0 END) FROM OrderEntity o")
    long sumTotalPricesForDeliveredOrders();
}

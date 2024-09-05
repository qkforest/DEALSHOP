package com.qkforest.orderservice.repository;

import com.qkforest.orderservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.orderStatus in (com.qkforest.orderservice.domain.OrderStatus.NEW_ORDER, com.qkforest.orderservice.domain.OrderStatus.IN_TRANSIT) and o.lastModifiedDate between :prevDay and :nextDay")
    List<Order> findAllBetweenPrevDayAndToday(@Param("prevDay") LocalDateTime prevDay, @Param("nextDay")LocalDateTime nextDay);
    List<Order> findOrderListByUserId(Long userId);
}

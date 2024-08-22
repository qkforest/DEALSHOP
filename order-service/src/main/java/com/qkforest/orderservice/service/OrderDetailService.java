package com.qkforest.orderservice.service;

import com.qkforest.orderservice.domain.Order;
import com.qkforest.orderservice.domain.OrderDetail;
import com.qkforest.orderservice.domain.OrderStatus;
import com.qkforest.orderservice.repository.OrderDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<OrderDetail> findAllByOrderIdOrElseThrow(Long orderId) {
        return orderDetailRepository.findAllByOrderId(orderId);
    }
}

package com.qkforest.orderservice.service;

import com.qkforest.orderservice.domain.OrderDetail;
import com.qkforest.orderservice.repository.OrderDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

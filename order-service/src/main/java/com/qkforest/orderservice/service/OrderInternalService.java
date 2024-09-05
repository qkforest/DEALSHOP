package com.qkforest.orderservice.service;

import com.qkforest.commonmodule.dto.order.OrderedProductPaymentRequest;
import com.qkforest.commonmodule.dto.product.request.OrderProductRequest;
import com.qkforest.commonmodule.dto.product.request.OrderSaveRequest;
import com.qkforest.orderservice.domain.Order;
import com.qkforest.orderservice.domain.OrderDetail;
import com.qkforest.orderservice.domain.OrderStatus;
import com.qkforest.orderservice.repository.OrderDetailRepository;
import com.qkforest.orderservice.repository.OrderRepository;
import com.qkforest.orderservice.service.event.OrderProductEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderInternalService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderInternalService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void saveOrder(OrderSaveRequest orderSaveRequest) {
        Long userId = orderSaveRequest.getUserId();
        Long productId = orderSaveRequest.getProductId();
        int quantity = orderSaveRequest.getQuantity();
        Long unitPrice = orderSaveRequest.getUnitPrice();
        Long totalPrice = orderSaveRequest.getTotalPrice();
        Order order = orderRepository.save(Order.builder()
                .userId(userId)
                .orderStatus(OrderStatus.NEW_ORDER)
                .totalPrice(totalPrice)
                .build());
        orderDetailRepository.save(OrderDetail.builder()
                .order(order)
                .productId(productId)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .build());
        applicationEventPublisher.publishEvent(new OrderProductEvent(this, new OrderedProductPaymentRequest(order.getId(), userId, totalPrice, new OrderProductRequest(productId, quantity))));
    }
}

package com.qkforest.orderservice.service;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class OrderStatusScheduler {

    private final OrderService orderService;

    public OrderStatusScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "0 50 20 * * ?")
    public void updateOrderStatus() {
        orderService.updateOrderStatusToInTransit();
        orderService.updateOrderStatusToShipped();
    }
}

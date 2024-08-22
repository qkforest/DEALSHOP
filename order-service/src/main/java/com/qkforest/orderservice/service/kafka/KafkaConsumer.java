package com.qkforest.orderservice.service.kafka;

import com.qkforest.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {
    private final OrderService orderService;

    public KafkaConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "product-failUpdateStock", groupId = "consumerGroupId")
    public void FailUpdateStock(String message) {
        orderService.handleFailUpdateStock(Long.parseLong(message));
        log.info("Kafka Message : ->" + message);
    }
}

package com.qkforest.orderservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkforest.commonmodule.dto.payment.OrderRollBackRequest;
import com.qkforest.commonmodule.dto.product.request.OrderSaveRequest;
import com.qkforest.orderservice.service.OrderInternalService;
import com.qkforest.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {
    private final OrderService orderService;
    private final OrderInternalService orderInternalService;

    public KafkaConsumer(OrderService orderService, OrderInternalService orderInternalService) {
        this.orderService = orderService;
        this.orderInternalService = orderInternalService;
    }

    @KafkaListener(topics = "dealshop.order.request",  containerFactory = "kafkaListenerContainerFactory")
    public void saveOrder(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            OrderSaveRequest orderSaveRequest = mapper.readValue(message, new TypeReference<>(){});
            orderInternalService.saveOrder(orderSaveRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "dealshop.order.rollback",  containerFactory = "kafkaListenerContainerFactory")
    public void orderRollBack(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            OrderRollBackRequest orderFailRequest = mapper.readValue(message, new TypeReference<>(){});
            orderService.updateOrderStatusToCancel(orderFailRequest.getOrderId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

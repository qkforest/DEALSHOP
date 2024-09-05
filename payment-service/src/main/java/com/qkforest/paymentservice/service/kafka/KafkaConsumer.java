package com.qkforest.paymentservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkforest.commonmodule.dto.order.OrderCancelRequest;
import com.qkforest.commonmodule.dto.order.OrderedProductPaymentRequest;
import com.qkforest.paymentservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    private final PaymentService paymentService;

    @Autowired
    public KafkaConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "dealshop.payment.request",  containerFactory = "kafkaListenerContainerFactory")
    public void receivePaymentRequestEvent(String message) {;
        ObjectMapper mapper = new ObjectMapper();
        try {
            OrderedProductPaymentRequest orderedProductPaymentRequest = mapper.readValue(message, new TypeReference<OrderedProductPaymentRequest>(){});
            paymentService.saveOrderedProductsPayment(orderedProductPaymentRequest);
            log.info("OrderId={}, 상품 결제 요청 이벤트 소비", orderedProductPaymentRequest.getOrderId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "dealshop.order.cancel",  containerFactory = "kafkaListenerContainerFactory")
    public void receiveOrderCancelEvent(String message) {;
        ObjectMapper mapper = new ObjectMapper();
        try {
            OrderCancelRequest orderCancelRequest = mapper.readValue(message, new TypeReference<OrderCancelRequest>(){});
            paymentService.refund(orderCancelRequest);
            log.info("OrderId={}, 상품 결제 요청 이벤트 소비", orderCancelRequest.getOrderId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

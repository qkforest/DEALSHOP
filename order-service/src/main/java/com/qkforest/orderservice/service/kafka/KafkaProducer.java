package com.qkforest.orderservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkforest.commonmodule.dto.order.OrderCancelRequest;
import com.qkforest.commonmodule.dto.order.OrderedProductPaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaPaymentTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaPaymentTemplate) {
        this.kafkaPaymentTemplate = kafkaPaymentTemplate;
    }

    public void sendPaymentRequestMessage(String topic, OrderedProductPaymentRequest savePaymentRequest) {
        ObjectMapper mapper = new ObjectMapper();
        String message = "";
        try {
            message = mapper.writeValueAsString(savePaymentRequest);
            kafkaPaymentTemplate.send(topic, message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    public void sendOrderCancelMessage(String topic, OrderCancelRequest orderCancelRequest) {
        ObjectMapper mapper = new ObjectMapper();
        String message = "";
        try {
            message = mapper.writeValueAsString(orderCancelRequest);
            kafkaPaymentTemplate.send(topic, message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


}

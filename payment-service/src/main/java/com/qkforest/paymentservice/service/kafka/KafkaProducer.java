package com.qkforest.paymentservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkforest.commonmodule.dto.payment.OrderRollBackRequest;
import com.qkforest.commonmodule.dto.payment.StockRollbackRequest;
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

    public void sendOrderRollbackMessage(String topic, OrderRollBackRequest orderFailRequest) {

        ObjectMapper mapper = new ObjectMapper();
        String message = "";
        try {
            message = mapper.writeValueAsString(orderFailRequest);
            kafkaPaymentTemplate.send(topic, message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void sendStockRollbackMessage(String topic, StockRollbackRequest stockRollbackRequest) {

        ObjectMapper mapper = new ObjectMapper();
        String message = "";
        try {
            message = mapper.writeValueAsString(stockRollbackRequest);
            kafkaPaymentTemplate.send(topic, message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

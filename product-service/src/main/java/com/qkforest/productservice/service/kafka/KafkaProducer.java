package com.qkforest.productservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkforest.commonmodule.dto.product.request.OrderSaveRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaProducerTemplate ;

    public KafkaProducer(KafkaTemplate<String, String> kafkaProducerTemplate) {
        this.kafkaProducerTemplate = kafkaProducerTemplate;
    }

    public void sendMessage(String topic, OrderSaveRequest orderSaveRequest) {
        ObjectMapper mapper = new ObjectMapper();
        String message = "";
        try {
            message = mapper.writeValueAsString(orderSaveRequest);
            kafkaProducerTemplate.send(topic, message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

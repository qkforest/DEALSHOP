package com.qkforest.orderservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkforest.commonmodule.dto.UpdateStockRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaUpdateStockTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaUpdateStockTemplate) {
        this.kafkaUpdateStockTemplate = kafkaUpdateStockTemplate;
    }

    public void sendUpdateStockRequest(String topic, UpdateStockRequest updateStockRequest) {
        ObjectMapper mapper = new ObjectMapper();
        String message = "";
        try {
            message = mapper.writeValueAsString(updateStockRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        kafkaUpdateStockTemplate.send(topic, message);
        log.info("Kafka Producer sent data from the order-service" + message);
    }
}

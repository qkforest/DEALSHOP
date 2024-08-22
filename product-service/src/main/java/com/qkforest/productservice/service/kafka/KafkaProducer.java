package com.qkforest.productservice.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaFailUpdateStockTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaFailUpdateStockTemplate) {
        this.kafkaFailUpdateStockTemplate = kafkaFailUpdateStockTemplate;
    }

    public void sendFailUpdateStockRequest(String topic, String message) {
        kafkaFailUpdateStockTemplate.send(topic, message);
        log.info("Kafka Producer sent data from the product-service" + message);
    }
}

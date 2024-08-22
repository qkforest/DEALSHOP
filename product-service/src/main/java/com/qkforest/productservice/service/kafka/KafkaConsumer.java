package com.qkforest.productservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkforest.commonmodule.dto.UpdateProductStockRequest;
import com.qkforest.commonmodule.dto.UpdateStockRequest;
import com.qkforest.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KafkaConsumer {

    private final ProductService productService;

    @Autowired
    public KafkaConsumer(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "order-updateStock", groupId = "consumerGroupId")
    public void updateStock(String message) {
        log.info("Kafka Message : -> " + message);
        ObjectMapper mapper = new ObjectMapper();
        try {
            UpdateStockRequest updateStockRequest = mapper.readValue(message, new TypeReference<>(){});
            if(updateStockRequest.getOption() == 1) {
                productService.checkStock(updateStockRequest);
            } else {
                productService.restock(updateStockRequest);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

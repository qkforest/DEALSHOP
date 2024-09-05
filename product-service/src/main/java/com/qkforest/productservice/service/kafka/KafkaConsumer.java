package com.qkforest.productservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkforest.commonmodule.dto.order.OrderCancelRequest;
import com.qkforest.commonmodule.dto.payment.StockRollbackRequest;
import com.qkforest.commonmodule.dto.product.request.OrderProductRequest;
import com.qkforest.productservice.service.ProductService;
import com.qkforest.productservice.service.RedissonLockFacade;
import com.qkforest.productservice.service.StockRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    private final ProductService productService;
    private final RedissonLockFacade redissonLockFacade;
    private final StockRedisService stockRedisService;

    public KafkaConsumer(ProductService productService, RedissonLockFacade redissonLockFacade, StockRedisService stockRedisService) {
        this.productService = productService;
        this.redissonLockFacade = redissonLockFacade;
        this.stockRedisService = stockRedisService;
    }

    @KafkaListener(topics = {"dealshop.stock.rollback"},  containerFactory = "kafkaListenerContainerFactory")
    public void RollbackStock(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            StockRollbackRequest stockRollbackrequest = mapper.readValue(message, new TypeReference<>(){});
            OrderProductRequest orderProductRequest = stockRollbackrequest.getOrderProductRequest();
            stockRedisService.restockWithLua(orderProductRequest.getProductId(), orderProductRequest.getQuantity());
            log.info("OrderId={}, 재고 롤백 이벤트 소비", stockRollbackrequest.getOrderId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = {"dealshop.order.cancel"},  containerFactory = "kafkaListenerContainerFactory")
    public void RollbackStockForOrderCancel(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            OrderCancelRequest orderCancelRequest = mapper.readValue(message, new TypeReference<OrderCancelRequest>(){});
            for(OrderProductRequest orderProductRequest : orderCancelRequest.getOrderProductRequestList()) {
                stockRedisService.restockWithLua(orderProductRequest.getProductId(), orderProductRequest.getQuantity());
            }
            log.info("OrderId={}, 주문 취소 재고 롤백 이벤트 소비", orderCancelRequest.getOrderId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

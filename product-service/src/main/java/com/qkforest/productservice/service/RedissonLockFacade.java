package com.qkforest.productservice.service;

import com.qkforest.commonmodule.dto.product.request.OrderProductRequest;
import com.qkforest.productservice.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Component
@Slf4j
public class RedissonLockFacade {

    private final String REDISSON_LOCK_PREFIX = "stock-lock:";

    private final RedissonClient redissonClient;
    private final ProductService productService;

    public RedissonLockFacade(RedissonClient redissonClient, ProductService productService) {
        this.redissonClient = redissonClient;
        this.productService = productService;
    }

    public void orderProduct(Long userId, OrderProductRequest orderProductRequest) {
        Long productId = orderProductRequest.getProductId();
        RLock lock = redissonClient.getLock(REDISSON_LOCK_PREFIX + productId);
        try {
            if (!lock.tryLock(60, 5, TimeUnit.SECONDS)) {
                log.error("상품 재고 감소 LOCK 획득 실패");
            }
            productService.orderProduct(userId, orderProductRequest);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void restock(OrderProductRequest orderProductRequest) {
        Long productId = orderProductRequest.getProductId();
        RLock lock = redissonClient.getLock(REDISSON_LOCK_PREFIX + productId);
        try {
            if (!lock.tryLock(60, 5, TimeUnit.SECONDS)) {
                log.error("상품 재고 증가 LOCK 획득 실패");
            }
            productService.restock(orderProductRequest);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}

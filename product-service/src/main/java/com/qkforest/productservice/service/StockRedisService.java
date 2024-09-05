package com.qkforest.productservice.service;

import com.qkforest.commonmodule.exception.BusinessLogicException;
import com.qkforest.commonmodule.exception.ExceptionCode;
import com.qkforest.productservice.domain.Product;
import com.qkforest.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Component
public class StockRedisService {
    private final String REDIS_PREFIX = "stock:";
    private final ProductRepository productRepository;
    private final ValueOperations<String, Integer> valueOperations;
    private final RedisTemplate<String, Integer> redisTemplate;

    public StockRedisService(RedisTemplate<String, Integer> redisTemplate, ProductRepository productRepository) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.productRepository = productRepository;
    }

    public void saveProductStock(Long productId, int stock) {
        valueOperations.setIfAbsent(REDIS_PREFIX + productId, stock);
        log.debug("상품이 등록되었습니다. 상품 ID: {}, 총 재고: {}", productId, stock);
    }

    public Integer getProductStock(Long productId) {
        String key = REDIS_PREFIX + productId;
        return valueOperations.get(key);
    }

    public void restock(Long productId, int quantity) {
        valueOperations.increment(REDIS_PREFIX + productId, quantity);
    }

    public void reduceStock(Long productId, int quantity) {
        valueOperations.decrement(REDIS_PREFIX + productId, quantity);
    }

    public void reduceStockWithLua(Long productId, int quantity) {
        String script = """
                local stock = redis.call('GET', KEYS[1])
                local quantity = tonumber(ARGV[1])
                if stock == nil then
                   return -2
                end
                if tonumber(stock) < quantity then
                    return -1
                end
                redis.call('DECRBY', KEYS[1], quantity)
                return 1
               """;

        Long result = redisTemplate.execute(
                new DefaultRedisScript<Long>(script, Long.class),
                Collections.singletonList(REDIS_PREFIX + productId), quantity);

        if (result == -1) {
            throw new BusinessLogicException(ExceptionCode.NOT_ENOUGH_STOCK);
        } else if (result == -2) {
            throw new BusinessLogicException(ExceptionCode.NO_SUCH_PRODUCT);
        }

    }


    public void restockWithLua(Long productId, Integer quantity) {
        String script = """
               local stock = redis.call('GET', KEYS[1])
               if stock == false then
                   return nil
               end
               stock = redis.call('INCRBY', KEYS[1], ARGV[1])
               return stock
             """;

        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                Collections.singletonList(REDIS_PREFIX + productId), quantity);

        if (result  == null) {
            throw new BusinessLogicException(ExceptionCode.NO_SUCH_PRODUCT);
        }
    }


    public Integer getProductStockWithLua(Long productId) {
        String script = """
                local stock = redis.call('get', KEYS[1])
                if stock == nil then
                    return nil
                end
                return stock
                """;

        Long result = redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(REDIS_PREFIX + productId));
        if (result == null) {
            throw new BusinessLogicException(ExceptionCode.NO_SUCH_PRODUCT);
        }
        return Math.toIntExact(result);
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void syncDB() {
        Set<String> keys = redisTemplate.keys(REDIS_PREFIX+"*");
        if (!keys.isEmpty()) {
            for (String key : keys) {
                int stock = valueOperations.get(key);
                Long productId = Long.parseLong(key.split(":")[1]);
                Product product = productRepository.findById(productId).orElseThrow(() ->  new BusinessLogicException(ExceptionCode.NO_SUCH_PRODUCT));
                product.updateStock(stock);
            }
        }
    }
}

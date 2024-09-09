package com.qkforest.productservice.service;

import com.qkforest.commonmodule.dto.product.request.OrderProductRequest;
import com.qkforest.productservice.domain.Product;
import com.qkforest.productservice.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private RedissonLockFacade redissonLockFacade;

    @Autowired
    private StockRedisService stockRedisService;

    @Autowired
    private ProductRepository productRepository;

    private Long productId;

    @BeforeEach
    public void before() {
        Product product = productRepository.saveAndFlush(Product.builder()
                .title("상품1")
                .price(10000L)
                .activationTime(LocalDateTime.now())
                .description("")
                .stock(500)
                .build());
        stockRedisService.saveProductStock(product.getId(), product.getStock());
        productId = product.getId();
    }

    @AfterEach
    public void after() {
        productRepository.deleteAll();
    }


    @Test
    @DisplayName("동시에 500개 주문 with 분산락 + 캐시")
    public void orderProductTest() throws InterruptedException {
        int threadCount = 500;

        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    Long user_id = (long) finalI;
                    OrderProductRequest orderProductRequest = new OrderProductRequest(productId, 1);
                    redissonLockFacade.orderProduct(user_id, orderProductRequest);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Product product = productRepository.findById(productId).orElseThrow();
        Assertions.assertEquals(0, product.getStock());
    }
}

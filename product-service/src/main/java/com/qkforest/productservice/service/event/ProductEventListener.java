package com.qkforest.productservice.service.event;

import com.qkforest.commonmodule.dto.product.request.OrderSaveRequest;
import com.qkforest.productservice.service.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ProductEventListener {

    private final KafkaProducer kafkaProducer;

    public ProductEventListener(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderRequestSuccess(OrderRequestEvent event) {
        try {
            OrderSaveRequest orderSaveRequest = event.getOrderSaveRequest();
            Long userId = orderSaveRequest.getUserId();
            Long productId = orderSaveRequest.getProductId();
            log.info("UserId={}, ProductId={}, 주문 생성 이벤트 발행", userId, productId);
            kafkaProducer.sendMessage("dealshop.order.request", orderSaveRequest);
        } catch (Exception e) {
            log.error("주문 생성 이벤트 발행 실패", e);
        }
    }


}

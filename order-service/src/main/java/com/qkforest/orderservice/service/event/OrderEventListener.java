package com.qkforest.orderservice.service.event;

import com.qkforest.commonmodule.dto.order.OrderCancelRequest;
import com.qkforest.commonmodule.dto.order.OrderedProductPaymentRequest;
import com.qkforest.orderservice.service.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class OrderEventListener {

    private final KafkaProducer kafkaProducer;

    public OrderEventListener(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderSuccess(OrderProductEvent event) {
        try {
            OrderedProductPaymentRequest orderedProductPaymentRequest = event.getOrderedProductPaymentRequest();
            Long orderId = orderedProductPaymentRequest.getOrderId();
            log.info("orderId={}, 상품 결제 요청 이벤트 발행", orderId);
            kafkaProducer.sendPaymentRequestMessage("dealshop.payment.request", orderedProductPaymentRequest);
        } catch (Exception e) {
            log.error("상품 결제 요청 이벤트 발행 실패", e);
        }
    }
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCancel(OrderCancelEvent event) {
        try {
            OrderCancelRequest orderCancelRequest = event.getOrderCancelRequest();
            Long orderId = orderCancelRequest.getOrderId();
            log.info("orderId={}, 주문 취소 이벤트 발행", orderId);
            kafkaProducer.sendOrderCancelMessage("dealshop.order.cancel", orderCancelRequest);
        } catch (Exception e) {
            log.error("주문 취소 이벤트 발행 실패", e);
        }
    }

}

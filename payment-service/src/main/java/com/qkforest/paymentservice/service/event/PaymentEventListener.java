package com.qkforest.paymentservice.service.event;

import com.qkforest.commonmodule.dto.payment.OrderRollBackRequest;
import com.qkforest.commonmodule.dto.order.OrderedProductPaymentRequest;
import com.qkforest.commonmodule.dto.payment.StockRollbackRequest;
import com.qkforest.paymentservice.service.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Slf4j
@Component
public class PaymentEventListener {

    public KafkaProducer kafkaProducer;


    public PaymentEventListener(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void onFailure(PaymentEvent event) {
        OrderedProductPaymentRequest orderedProductPaymentRequest = event.getSavePaymentRequest();
        log.info("OrderId={}, 상품 결제 실패 이벤트 발행", orderedProductPaymentRequest.getOrderId());
        kafkaProducer.sendOrderRollbackMessage("dealshop.order.rollback",
                OrderRollBackRequest.from(orderedProductPaymentRequest));
        kafkaProducer.sendStockRollbackMessage("dealshop.stock.rollback",
                StockRollbackRequest.from(orderedProductPaymentRequest));
    }
}

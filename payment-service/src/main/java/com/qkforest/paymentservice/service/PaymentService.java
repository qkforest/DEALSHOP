package com.qkforest.paymentservice.service;

import com.qkforest.commonmodule.dto.order.OrderCancelRequest;
import com.qkforest.paymentservice.domain.PaymentStatus;
import com.qkforest.commonmodule.dto.order.OrderedProductPaymentRequest;
import com.qkforest.paymentservice.domain.Payment;
import com.qkforest.paymentservice.repository.PaymentRepository;
import com.qkforest.paymentservice.service.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final Random random = new Random();
    private final ApplicationEventPublisher applicationEventPublisher;


    public PaymentService(PaymentRepository paymentRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.paymentRepository = paymentRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void saveOrderedProductsPayment(OrderedProductPaymentRequest orderedProductPaymentRequest) {
        Payment payment = paymentRepository.save(Payment.from(orderedProductPaymentRequest));
        if (processPayment(payment, PaymentStatus.IN_PROGRESS,"고객 변심 이탈") || processPayment(payment, PaymentStatus.COMPLETE,"결제 실패 이탈")) {
            log.info("OrderId={}, 결제 실패 이벤트 발행", orderedProductPaymentRequest.getOrderId());
            applicationEventPublisher.publishEvent(new PaymentEvent(this, orderedProductPaymentRequest));
            return;
        }
        log.info("OrderId={}, 결제 성공", orderedProductPaymentRequest.getOrderId());
    }

    private boolean processPayment(Payment payment, PaymentStatus paymentStatus, String message) {
        if (random.nextDouble() < 0.2) {
            payment.updatePaymentStatus(PaymentStatus.BOUNCE);
            log.info(message);
            return true;
        }
        payment.updatePaymentStatus(paymentStatus);
        return false;
    }

    @Transactional
    public void refund(OrderCancelRequest orderCancelRequest) {
        Long orderId = orderCancelRequest.getOrderId();
        paymentRepository.updatePaymentStatusByOrderId(orderId, PaymentStatus.REFUNDED);
    }
}
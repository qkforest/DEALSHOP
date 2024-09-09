package com.qkforest.paymentservice.domain;

import com.qkforest.commonmodule.domain.BaseEntity;
import com.qkforest.commonmodule.dto.order.OrderedProductPaymentRequest;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payments")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "payment_amount", nullable = false)
    private Long paymentAmount;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "user_id", nullable = false)
    private Long userId;


    public Payment( Long paymentAmount, PaymentStatus paymentStatus, Long orderId, Long userId) {
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
        this.orderId = orderId;
        this.userId = userId;
    }

    public static Payment from(OrderedProductPaymentRequest savePaymentRequest) {
        return new Payment(savePaymentRequest.getTotalPrice(), PaymentStatus.UNPAID, savePaymentRequest.getOrderId(), savePaymentRequest.getUserId());
    }

    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}

package com.qkforest.paymentservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {

    UNPAID("결제 전"),
    IN_PROGRESS("결제 중"),
    COMPLETE("결제 완료"),
    BOUNCE("결제 이탈"),
    FAILED("결제 실패"),
    REFUNDED("환불 완료");

    private final String value;
}

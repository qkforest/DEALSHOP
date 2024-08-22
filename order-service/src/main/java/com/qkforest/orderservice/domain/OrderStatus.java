package com.qkforest.orderservice.domain;

import lombok.*;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    NEW_ORDER("결제 완료됨"),
    FAILED("주문 실패"),
    CANCELLED("취소됨"),
    IN_TRANSIT("배송중"),
    SHIPPED("배송 완료"),
    RETURNED("반품됨");

    private final String value;
}

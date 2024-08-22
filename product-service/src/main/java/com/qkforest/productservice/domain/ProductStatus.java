package com.qkforest.productservice.domain;

import lombok.Getter;

@Getter
public enum ProductStatus {
    PREPARING("판매대기"),
    ON_SALE("판매중"),
    SOLD_OUT("품절");

    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }

}

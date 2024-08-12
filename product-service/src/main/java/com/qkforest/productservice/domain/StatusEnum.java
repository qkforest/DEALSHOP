package com.qkforest.productservice.domain;

public enum StatusEnum {
    WAIT("판매대기"),
    ON_SALE("판매중"),
    OUT_OF_STOCK("품절");

    private final String status;

    StatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}

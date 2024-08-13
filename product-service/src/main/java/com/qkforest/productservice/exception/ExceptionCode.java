package com.qkforest.productservice.exception;
import lombok.Getter;

@Getter
public enum ExceptionCode {
    WISHLIST_EXISTS(409, "장바구니에 이미 존재하는 상품입니다."),
    INVALID_UPDATE_REQUEST(409, "유효하지 않은 상품 수량 변경 요청입니다."),
    NOT_FOUND_WISHLIST(409, "유효하지 않은 삭제 요청입니다.");

    private final int status;
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
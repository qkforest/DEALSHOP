package com.qkforest.commonmodule.exception;
import lombok.Getter;

@Getter
public enum ExceptionCode {

    // User
    NO_SUCH_ALGORITHM(409, "인증 번호 생성을 위한 알고리즘을 찾을 수 없습니다."),
    UNABLE_TO_SEND_EMAIL(409, "메일을 전송할 수 없습니다."),
    USER_EMAIL_EXISTS(409, "이미 사용 중인 이메일입니다"),
    EMAIL_CODE_IS_NOT_SAME(409, "인증 번호가 일치하지 않습니다."),
    NO_SUCH_USER(409,"해당 사용자가 존재하지 않습니다."),

    // Product
    WISHLIST_EXISTS(409, "장바구니에 이미 존재하는 상품입니다."),
    INVALID_UPDATE_REQUEST(409, "유효하지 않은 상품 수량 변경 요청입니다."),
    NO_SUCH_WISHLIST(409, "해당 장바구니가 존재하지 않습니다."),
    NOT_AVAILABLE_TIME(409, "주문이 불가능한 시간입니다."),
    NO_SUCH_PRODUCT(409, "해당 상품이 존재하지 않습니다."),
    NOT_ENOUGH_STOCK(409, "재고가 충분하지 않습니다."),

    // Order
    NO_SUCH_ORDER(409,"해당 주문이 존재하지 않습니다."),
    NOT_AVAILABLE_USER(409, "해당 주문에 대한 권한이 없는 사용자 입니다."),
    CANCEL_NOT_APPROVED(409, "취소가 불가능한 주문입니다."), 
    RETURN_NOT_APPROVED(409, "반품이 불가능한 주문입니다.");

    private final int status;
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
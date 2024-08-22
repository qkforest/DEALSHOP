package com.qkforest.commonmodule.exception;
import lombok.Getter;

@Getter
public enum ExceptionCode {

    // User
    NO_SUCH_ALGORITHM(400, "인증 번호 생성을 위한 알고리즘을 찾을 수 없습니다."),
    UNABLE_TO_SEND_EMAIL(404, "메일을 전송할 수 없습니다."),
    USER_EMAIL_EXISTS(409, "이미 사용 중인 이메일입니다"),
    EMAIL_CODE_IS_NOT_SAME(409, "인증 번호가 일치하지 않습니다."),


    // Product
    WISHLIST_EXISTS(409, "장바구니에 이미 존재하는 상품입니다."),
    INVALID_UPDATE_REQUEST(409, "유효하지 않은 상품 수량 변경 요청입니다."),
    NOT_FOUND_WISHLIST(409, "유효하지 않은 삭제 요청입니다."),
    NO_PRODUCT_OPTION(409, "상품 옵션이 없습니다."),


    // Order
    CANCEL_NOT_APPROVED(409, "취소가 불가능한 주문입니다."), 
    RETURN_NOT_APPROVED(409, "반품이 불가능한 주문입니다.");

    private final int status;
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
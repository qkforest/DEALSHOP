package com.qkforest.userservice.exception;
import lombok.Getter;

@Getter
public enum ExceptionCode {

    // Member
    NO_SUCH_ALGORITHM(400, "인증 번호 생성을 위한 알고리즘을 찾을 수 없습니다."),
    UNABLE_TO_SEND_EMAIL(404, "메일을 전송할 수 없습니다."),
    MEMBER_EMAIL_EXISTS(409, "이미 사용 중인 이메일입니다"),
    EMAIL_CODE_IS_NOT_SAME(409, "인증 번호가 일치하지 않습니다.");

    private final int status;
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
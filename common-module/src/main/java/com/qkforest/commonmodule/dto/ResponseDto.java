package com.qkforest.commonmodule.dto;

import lombok.*;

@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {

    private final Integer code; // 1 성공, -1 실패
    private final String msg;
    private final T data;
}
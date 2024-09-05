package com.qkforest.commonmodule.dto;

import lombok.*;

@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {

    private final Integer status;
    private final String message;
    private final T data;
}
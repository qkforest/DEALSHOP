package com.qkforest.commonmodule.exception;

import com.qkforest.commonmodule.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler({BusinessLogicException.class})
    public ResponseEntity<?> handleBusinessLogicException(BusinessLogicException e) {
        return new ResponseEntity<>(new ResponseDto<>(e.getExceptionCode().getStatus(), e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
}

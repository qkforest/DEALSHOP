package com.qkforest.orderservice.controller;

import com.qkforest.commonmodule.dto.ResponseDto;
import com.qkforest.commonmodule.exception.ExceptionCode;
import com.qkforest.orderservice.domain.OrderStatus;
import com.qkforest.orderservice.dto.reponse.OrderDetailsResponse;
import com.qkforest.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/auth")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrderList(@RequestHeader("X_User_Id") Long userId) {
        List<OrderDetailsResponse> result = orderService.getOrderList(userId);
        return new ResponseEntity<>(new ResponseDto<>(200, "주문 내역 조회에 성공했습니다.", result), HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrderDetails(@RequestHeader("X_User_Id") Long userId,
                                             @PathVariable Long orderId) {
        OrderDetailsResponse result = orderService.getOrderDetails(userId, orderId);
        return new ResponseEntity<>(new ResponseDto<>(200, "주문 상세 조회에 성공했습니다.", result), HttpStatus.OK);
    }

    @PostMapping("/orders/{orderId}")
    public ResponseEntity<?> cancelOrder(@RequestHeader("X_User_Id") Long userId,
                                         @PathVariable Long orderId) {
        orderService.cancelOrder2(userId, orderId, OrderStatus.NEW_ORDER, OrderStatus.CANCELLED, ExceptionCode.CANCEL_NOT_APPROVED);
        return new ResponseEntity<>(new ResponseDto<>(200, "주문 취소에 성공했습니다.", orderId), HttpStatus.OK);
    }

    @PostMapping("/returns/{orderId}")
    public ResponseEntity<?> returnProducts(@RequestHeader("X_User_Id") Long userId,
                                            @PathVariable Long orderId) {
        orderService.cancelOrder2(userId, orderId, OrderStatus.SHIPPED, OrderStatus.RETURNED, ExceptionCode.RETURN_NOT_APPROVED);
        return new ResponseEntity<>(new ResponseDto<>(200, "반품에 성공했습니다.", orderId), HttpStatus.OK);
    }
}

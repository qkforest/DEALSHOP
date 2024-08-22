package com.qkforest.orderservice.controller;

import com.qkforest.commonmodule.dto.ResponseDto;
import com.qkforest.orderservice.dto.reponse.OrderDetailsResponse;
import com.qkforest.orderservice.dto.request.OrderSaveRequest;
import com.qkforest.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<?> saveOrder(@RequestHeader("X_User_Id") Long userId
                                        , @RequestBody OrderSaveRequest orderSaveRequest) {
        Long result = orderService.saveOrder(userId, orderSaveRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "주문 내역을 확인하세요.", result), HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        OrderDetailsResponse result = orderService.getOrderDetail(orderId);
        return new ResponseEntity<>(new ResponseDto<>(1, "주문 상세 조회에 성공했습니다.", result), HttpStatus.OK);
    }

    @PostMapping("/orders/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(new ResponseDto<>(1, "주문 취소에 성공했습니다.", orderId), HttpStatus.OK);
    }

    @PostMapping("/returns/{orderId}")
    public ResponseEntity<?> returnProducts(@PathVariable Long orderId) {
        orderService.returnProducts(orderId);
        return new ResponseEntity<>(new ResponseDto<>(1, "반품에 성공했습니다.", orderId), HttpStatus.OK);
    }
}

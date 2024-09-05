package com.qkforest.productservice.service.event;

import com.qkforest.commonmodule.dto.product.request.OrderSaveRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderRequestEvent extends ApplicationEvent {
    private final OrderSaveRequest orderSaveRequest;

    public OrderRequestEvent(Object source, OrderSaveRequest orderSaveRequest) {
        super(source);
        this.orderSaveRequest = orderSaveRequest;
    }
}

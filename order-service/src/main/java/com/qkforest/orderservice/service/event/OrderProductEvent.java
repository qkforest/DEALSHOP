package com.qkforest.orderservice.service.event;

import com.qkforest.commonmodule.dto.order.OrderedProductPaymentRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderProductEvent extends ApplicationEvent {
    private final OrderedProductPaymentRequest orderedProductPaymentRequest;

    public OrderProductEvent(Object source, OrderedProductPaymentRequest orderedProductPaymentRequest) {
        super(source);
        this.orderedProductPaymentRequest = orderedProductPaymentRequest;
    }
}

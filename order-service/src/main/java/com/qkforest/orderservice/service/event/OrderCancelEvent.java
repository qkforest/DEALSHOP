package com.qkforest.orderservice.service.event;

import com.qkforest.commonmodule.dto.order.OrderCancelRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderCancelEvent extends ApplicationEvent {
    private final OrderCancelRequest orderCancelRequest;

    public OrderCancelEvent(Object source, OrderCancelRequest orderCancelRequest) {
        super(source);
        this.orderCancelRequest = orderCancelRequest;
    }
}

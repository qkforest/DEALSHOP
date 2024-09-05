package com.qkforest.paymentservice.service.event;

import com.qkforest.commonmodule.dto.order.OrderedProductPaymentRequest;
import org.springframework.context.ApplicationEvent;

public class PaymentEvent  extends ApplicationEvent {

    private final OrderedProductPaymentRequest savePaymentRequest;

    public PaymentEvent(Object source, OrderedProductPaymentRequest savePaymentRequest) {
        super(source);
        this.savePaymentRequest = savePaymentRequest;
    }

    public OrderedProductPaymentRequest getSavePaymentRequest() {
        return savePaymentRequest;
    }
}

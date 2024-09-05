package com.qkforest.commonmodule.dto.payment;


import com.qkforest.commonmodule.dto.order.OrderedProductPaymentRequest;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderRollBackRequest {

    private Long userId;
    private Long orderId;

    public static OrderRollBackRequest from(OrderedProductPaymentRequest savePaymentRequest) {
        return new OrderRollBackRequest(savePaymentRequest.getUserId(), savePaymentRequest.getOrderId());
    }
}

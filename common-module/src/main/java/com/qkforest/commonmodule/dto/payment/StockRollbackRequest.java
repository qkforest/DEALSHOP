package com.qkforest.commonmodule.dto.payment;

import com.qkforest.commonmodule.dto.order.OrderedProductPaymentRequest;
import com.qkforest.commonmodule.dto.product.request.OrderProductRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockRollbackRequest {
    private Long orderId;
    private OrderProductRequest orderProductRequest;

    public static StockRollbackRequest from(OrderedProductPaymentRequest orderedProductPaymentRequest ) {
        return new StockRollbackRequest(orderedProductPaymentRequest.getOrderId(), orderedProductPaymentRequest.getOrderProductRequest());
    }
}

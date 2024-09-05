package com.qkforest.commonmodule.dto.product.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class OrderSaveRequest {
    private final Long userId;
    private final Long productId;
    private final Integer quantity;
    private final Long unitPrice;
    private final Long totalPrice;
}

package com.qkforest.commonmodule.dto.product.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockRollbackRequest {
    private Long productOptionId;
    private int quantity;
}

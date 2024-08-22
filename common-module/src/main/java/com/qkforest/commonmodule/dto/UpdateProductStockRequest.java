package com.qkforest.commonmodule.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateProductStockRequest {
    private Long productId;
    private Long productOptionId;
    private int quantity;
}

package com.qkforest.commonmodule.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStockRequest {
    private int option; // 1 : 재고 감소, 2: 재고 증가
    private Long OrderId;
    private List<UpdateProductStockRequest> updateProductStockRequestList;
}

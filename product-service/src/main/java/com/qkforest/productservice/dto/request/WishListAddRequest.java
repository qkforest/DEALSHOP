package com.qkforest.productservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishListAddRequest {
    @NotNull(message = "상품을 선택해 주세요")
    private Long productId;
    @NotNull(message = "상품 수량을 입력해 주세요")
    private int quantity;
}

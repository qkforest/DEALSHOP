package com.qkforest.productservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishListUpdateRequest {
    @NotNull(message = "상품 수량을 입력해 주세요")
    private int quantity;
}

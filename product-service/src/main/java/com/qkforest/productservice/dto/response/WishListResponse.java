package com.qkforest.productservice.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
public class WishListResponse {
    private Long id;
    private Long productId;
    private int quantity;
}

package com.qkforest.productservice.dto.response;

import com.qkforest.productservice.domain.Product;
import lombok.*;

@Data
@AllArgsConstructor
public class WishListUpdateResponse {
    private Long id;
    private Long productId;
    private int quantity;
}

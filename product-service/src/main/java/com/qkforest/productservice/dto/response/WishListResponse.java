package com.qkforest.productservice.dto.response;

import com.qkforest.productservice.domain.Product;
import com.qkforest.productservice.domain.WishList;
import lombok.*;

@Builder
@Getter
@Data
@AllArgsConstructor
public class WishListResponse {

    private Long id;
    private Long userId;
    private String name;
    private int quantity;

    /*public static WishListResponse from(WishList wishList) {
        return WishListResponse.builder()
                .productId(wishList.getProduct().getId())
                .userId(wishList.getUserId())
                .quantity(wishList.getQuantity())
                .build();
    }*/
}

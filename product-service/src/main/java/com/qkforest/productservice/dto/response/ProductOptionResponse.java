package com.qkforest.productservice.dto.response;

import com.qkforest.productservice.domain.ProductOption;

import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionResponse {
    private Long productOptionId;
    private String name;
    private Long additionalPrice;
    private int stock;

    public static ProductOptionResponse from(ProductOption productOption) {
        return ProductOptionResponse.builder()
                .productOptionId(productOption.getId())
                .name(productOption.getName())
                .additionalPrice(productOption.getAdditionalPrice())
                .build();
    }

    public static List<ProductOptionResponse> from(List<ProductOption> productOptionList) {
        return productOptionList.stream()
                .map(ProductOptionResponse::from)
                .collect(Collectors.toList());
    }
}
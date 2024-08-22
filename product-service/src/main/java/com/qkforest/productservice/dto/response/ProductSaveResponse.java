package com.qkforest.productservice.dto.response;

import com.qkforest.productservice.domain.Product;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaveResponse {
    private String productName;
    private String description;
    private Long price;
    private LocalDateTime activation_time;
    private List<ProductOptionResponse> productOptionResponseList;

    public static ProductSaveResponse from(Product product, List<ProductOptionResponse> productOptionResponseList) {
        return ProductSaveResponse.builder()
                .productName(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .activation_time(product.getActivation_time())
                .productOptionResponseList(productOptionResponseList)
                .build();
    }
}

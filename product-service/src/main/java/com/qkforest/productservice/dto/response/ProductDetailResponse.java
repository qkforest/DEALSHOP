package com.qkforest.productservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qkforest.productservice.domain.Product;
import com.qkforest.productservice.domain.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ProductDetailResponse {

    private Long product_id;
    private String name;
    private Long price;
    private String description;
    private ProductStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime activation_time;

    public static ProductDetailResponse from(Product product) {
        return ProductDetailResponse.builder()
                .product_id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .status(product.getProductStatus())
                .activation_time(product.getActivation_time())
                .build();
    }
}

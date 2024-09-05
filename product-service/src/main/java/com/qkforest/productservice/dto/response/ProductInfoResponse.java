package com.qkforest.productservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qkforest.productservice.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ProductInfoResponse {

    private Long product_id;
    private String product_title;
    private Long price;
    private String description;
    private int quantity;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime activation_time;

    public static ProductInfoResponse from(Product product) {
        return ProductInfoResponse.builder()
                .product_id(product.getId())
                .product_title(product.getTitle())
                .price(product.getPrice())
                .description(product.getDescription())
                .quantity(product.getStock())
                .activation_time(product.getActivation_time())
                .build();
    }
}

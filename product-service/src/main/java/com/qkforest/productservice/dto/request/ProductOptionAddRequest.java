package com.qkforest.productservice.dto.request;

import lombok.Data;

@Data
public class ProductOptionAddRequest {
    private String name;
    private int stock;
    private long additionalPrice;
}

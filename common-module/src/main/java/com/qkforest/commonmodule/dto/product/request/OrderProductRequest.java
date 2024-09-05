package com.qkforest.commonmodule.dto.product.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderProductRequest implements Serializable {
    @NotNull
    private Long productId;
    @NotNull
    @Min(value = 1)
    private int quantity;
}


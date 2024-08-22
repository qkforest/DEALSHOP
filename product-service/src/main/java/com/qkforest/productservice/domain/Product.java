package com.qkforest.productservice.domain;

import com.qkforest.commonmodule.domain.BaseEntity;
import com.qkforest.productservice.dto.request.ProductSaveRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @Column(nullable = false)
    private LocalDateTime activation_time;

    public static Product from(ProductSaveRequest product, ProductStatus productStatus, LocalDateTime activationTime) {
        return Product.builder()
                .name(product.getProductName())
                .price(product.getPrice())
                .description(product.getDescription())
                .productStatus(productStatus)
                .activation_time(activationTime)
                .build();
    }
}

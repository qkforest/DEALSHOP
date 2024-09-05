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
    private String title;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private LocalDateTime activation_time;


    public static Product from(ProductSaveRequest request, LocalDateTime activationTime) {
        return Product.builder()
                .title(request.getTitle())
                .price(request.getPrice())
                .description(request.getDescription())
                .activation_time(activationTime)
                .stock(request.getStock())
                .build();
    }

    public void updateStock(int quantity) {
        this.stock = quantity;
    }
}

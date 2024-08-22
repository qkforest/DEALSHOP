package com.qkforest.productservice.domain;

import com.qkforest.commonmodule.domain.BaseEntity;
import com.qkforest.productservice.dto.request.ProductOptionAddRequest;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_option")
public class ProductOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "additional_price", nullable = false)
    private Long additionalPrice;

    @Column(nullable = false)
    private int stock;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    public static ProductOption from(ProductOptionAddRequest productOptionAddRequest, Product product) {
        return ProductOption.builder()
                .name(productOptionAddRequest.getName())
                .additionalPrice(productOptionAddRequest.getAdditionalPrice())
                .stock(productOptionAddRequest.getStock())
                .product(product)
                .build();
    }


    public void updateStock(int quantity) {
        this.stock += quantity;
    }
}

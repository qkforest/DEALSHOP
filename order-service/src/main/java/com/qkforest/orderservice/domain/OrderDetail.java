package com.qkforest.orderservice.domain;

import com.qkforest.commonmodule.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.bytecode.enhance.spi.EnhancementContext;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_detail")
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long productOptionId;

    private int quantity;

    private Long unitPrice;

    @Builder
    public OrderDetail(Long id, Order order, Long productId, Long productOptionId, int quantity, Long unitPrice ) {
        this.id = id;
        this.order = order;
        this.productId = productId;
        this.productOptionId = productOptionId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }


}

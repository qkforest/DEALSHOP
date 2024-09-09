package com.qkforest.orderservice.domain;

import com.qkforest.commonmodule.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "total_price",nullable = false)
    private Long totalPrice;

    @Column(nullable = false)
    private Long userId;

    @Builder
    public Order(Long id, OrderStatus orderStatus, Long totalPrice, Long userId) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.totalPrice = totalPrice;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}

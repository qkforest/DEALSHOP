package com.qkforest.orderservice.dto.reponse;

import com.qkforest.commonmodule.dto.product.response.FeignOrderDetailResponse;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailsResponse {
    private Long orderId;
    private Long totalPrice;
    private String orderStatus;
    private String createdDate;
    private List<FeignOrderDetailResponse> orderDetails;

    public OrderDetailsResponse(Long orderId, Long totalPrice, String orderStatus, LocalDateTime createdDate, List<FeignOrderDetailResponse> orderDetails) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.createdDate = createdDate.toString();
        this.orderDetails = orderDetails;
    }
}

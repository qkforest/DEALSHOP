package com.qkforest.orderservice.dto.reponse;

import com.qkforest.commonmodule.dto.FeignProductDetailResponse;
import com.qkforest.orderservice.domain.OrderStatus;
import lombok.Data;

import java.util.List;


@Data
public class OrderDetailsResponse {
    private Long totalPrice;
    private String orderStatus;
    private List<FeignProductDetailResponse> orderDetail;

    public OrderDetailsResponse(Long totalPrice, String orderStatus, List<FeignProductDetailResponse> orderDetail) {
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.orderDetail = orderDetail;
    }
}

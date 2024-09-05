package com.qkforest.orderservice.dto.request;

import com.qkforest.commonmodule.dto.product.request.OrderProductRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderSaveRequest  {

    private List<OrderProductRequest> orderProductRequestList;
}

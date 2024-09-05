package com.qkforest.commonmodule.dto.order;

import com.qkforest.commonmodule.dto.product.request.OrderProductRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelRequest {
    private Long OrderId;
    private List<OrderProductRequest> orderProductRequestList;
}

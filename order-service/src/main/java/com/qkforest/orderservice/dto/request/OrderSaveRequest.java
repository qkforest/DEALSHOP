package com.qkforest.orderservice.dto.request;

import com.qkforest.commonmodule.dto.UpdateProductStockRequest;
import lombok.AllArgsConstructor;
import lombok.*;
import java.util.List;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderSaveRequest  {
    private List<UpdateProductStockRequest> orderProductRequestList;
}

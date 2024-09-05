package com.qkforest.commonmodule.dto.order;
import com.qkforest.commonmodule.dto.product.request.OrderProductRequest;
import lombok.*;
import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class OrderedProductPaymentRequest implements Serializable {
    private final Long orderId;
    private final Long userId;
    private final Long totalPrice;
    private final OrderProductRequest orderProductRequest;

}

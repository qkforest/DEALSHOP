package com.qkforest.commonmodule.dto.product.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeignOrderDetailResponse {

    private Long productId;
    private String productTitle;
    private Long price;
    private int quantity;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime activationTime;


}

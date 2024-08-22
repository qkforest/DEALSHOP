package com.qkforest.commonmodule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FeignProductDetailResponse {

    private Long product_id;
    private Long product_option_id;
    private String name;
    private String option_name;
    private Long price;
    private String description;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime activation_time;

    public FeignProductDetailResponse(Long product_id, Long product_option_id, String name, String option_name, Long price, String description, String status, LocalDateTime activation_time) {
        this.product_id = product_id;
        this.product_option_id = product_option_id;
        this.name = name;
        this.option_name = option_name;
        this.price = price;
        this.description = description;
        this.status = status;
        this.activation_time = activation_time;
    }

}

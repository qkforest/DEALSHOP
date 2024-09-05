package com.qkforest.productservice.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductSaveRequest {

        @Size(max = 50, message = "상품명은 50자 이하로 작성해 주세요")
        @NotBlank(message = "상품명을 입력해 주세요")
        private String title;

        @Size(max = 255, message = "내용은 255자 이하로 작성해 주세요")
        private String description;

        @NotNull(message = "가격을 입력해 주세요")
        private Long price;

        @NotNull(message = "상품 수량을 입력해주세요.")
        private int stock;

        @Nullable
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @FutureOrPresent(message = "예약 시작 시간은 현재 이후로 설정해 주세요")
        private LocalDateTime activation_time;

}

package com.qkforest.orderservice.client;

import com.qkforest.commonmodule.dto.FeignProductDetailResponse;
import com.qkforest.commonmodule.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @PostMapping("api/internal/products/detail")
    ResponseEntity<ResponseDto<List<FeignProductDetailResponse>>> getProductDetailsByIds(@RequestBody Set<Long> productOptionIds);
}
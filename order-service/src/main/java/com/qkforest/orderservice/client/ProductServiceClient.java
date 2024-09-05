package com.qkforest.orderservice.client;

import com.qkforest.commonmodule.dto.ResponseDto;
import com.qkforest.commonmodule.dto.product.response.FeignOrderDetailResponse;
import com.qkforest.commonmodule.dto.product.response.FeignProductInfosResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @PostMapping("api/internal/products/detail/order")
    ResponseEntity<ResponseDto<List<FeignOrderDetailResponse>>> getOrderDetailsByIdsAndQuantities(@RequestBody Map<Long, Integer> productIdsAndQuantities);

    @PostMapping("api/internal/products/detail")
    ResponseEntity<ResponseDto<List<FeignProductInfosResponse>>>  getProductInfosByIds(@RequestBody Set<Long> productIds);
}
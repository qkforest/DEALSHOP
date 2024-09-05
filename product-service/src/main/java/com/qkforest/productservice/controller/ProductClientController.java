package com.qkforest.productservice.controller;

import com.qkforest.commonmodule.dto.ResponseDto;
import com.qkforest.commonmodule.dto.product.response.FeignOrderDetailResponse;
import com.qkforest.commonmodule.dto.product.response.FeignProductInfosResponse;
import com.qkforest.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/internal")
@Slf4j
public class ProductClientController {

    private final ProductService productService;

    public ProductClientController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products/detail/order")
    public ResponseEntity<?> getOrderProductDetail(@RequestBody Map<Long, Integer> productIdsAndQuantities) {
        List<FeignOrderDetailResponse> result = productService.getOrderProductInfos(productIdsAndQuantities);
        return new ResponseEntity<>(new ResponseDto<>(1, "주문 상세 조회에 성공했습니다.", result), HttpStatus.OK);
    }

    @PostMapping("/products/detail")
    public ResponseEntity<ResponseDto<List<FeignProductInfosResponse>>> getProductInfosByIds(@RequestBody Set<Long> productIds){
        List<FeignProductInfosResponse> result = productService.getProductInfosByIds(productIds);
        return new ResponseEntity<>(new ResponseDto<>(1, "상품 상세 조회에 성공했습니다.", result), HttpStatus.OK);
    }

}

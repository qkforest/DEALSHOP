package com.qkforest.productservice.controller;

import com.qkforest.commonmodule.dto.FeignProductDetailResponse;
import com.qkforest.productservice.dto.response.ResponseDto;
import com.qkforest.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/internal")
@Slf4j
public class ProductClientController {

    private final ProductService productService;

    public ProductClientController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products/detail")
    public ResponseEntity<?> getOrderProductDetail(@RequestBody Set<Long> productOptionIds) {
        List<FeignProductDetailResponse> result = productService.getOrderProductDetail(productOptionIds);
        return new ResponseEntity<>(new ResponseDto<>(1, "주문 상세 조회에 성공했습니다.", result), HttpStatus.OK);
    }
}

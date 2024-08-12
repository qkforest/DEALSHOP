package com.qkforest.productservice.controller;

import com.qkforest.productservice.dto.response.ProductDetailResponse;
import com.qkforest.productservice.dto.response.ProductListResponse;
import com.qkforest.productservice.dto.response.ResponseDto;
import com.qkforest.productservice.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProductList(Pageable pageable) {
        Page<ProductListResponse> result = productService.getAllProductList(pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "상품 목록 조회에 성공했습니다.", result), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable("id") Long productId) {
        ProductDetailResponse result = productService.getProductDetail(productId);
        return new ResponseEntity<>(new ResponseDto<>(1, "상품 상세 조회에 성공했습니다.", result), HttpStatus.OK);
    }

}

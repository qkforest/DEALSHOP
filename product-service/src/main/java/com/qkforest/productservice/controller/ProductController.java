package com.qkforest.productservice.controller;

import com.qkforest.productservice.dto.request.ProductSaveRequest;
import com.qkforest.productservice.dto.response.ProductDetailResponse;
import com.qkforest.productservice.dto.response.ProductListResponse;
import com.qkforest.productservice.dto.response.ProductSaveResponse;
import com.qkforest.productservice.dto.response.ResponseDto;
import com.qkforest.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    public ResponseEntity<?> SaveProduct(@RequestBody ProductSaveRequest productSaveRequest) {
        ProductSaveResponse result = productService.saveProduct(productSaveRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "상품 저장에 성공했습니다.", result), HttpStatus.OK);
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

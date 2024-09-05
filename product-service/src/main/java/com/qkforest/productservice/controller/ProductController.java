package com.qkforest.productservice.controller;

import com.qkforest.commonmodule.dto.ResponseDto;
import com.qkforest.commonmodule.dto.product.request.OrderProductRequest;
import com.qkforest.productservice.dto.request.ProductSaveRequest;
import com.qkforest.productservice.dto.response.ProductInfoResponse;
import com.qkforest.productservice.service.ProductService;
import com.qkforest.productservice.service.RedissonLockFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/auth/products")
    public ResponseEntity<?> SaveProduct(@RequestBody ProductSaveRequest productSaveRequest) {
        ProductInfoResponse result = productService.saveProduct(productSaveRequest);
        return new ResponseEntity<>(new ResponseDto<>(200, "상품 저장에 성공했습니다.", result), HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProductList(Pageable pageable) {
        Page<ProductInfoResponse> result = productService.getProductList(pageable);
        return new ResponseEntity<>(new ResponseDto<>(200, "상품 목록 조회에 성공했습니다.", result), HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProductInfo(@PathVariable("productId") Long productId) {
        ProductInfoResponse result = productService.getProductInfo(productId);
        return new ResponseEntity<>(new ResponseDto<>(200, "상품 상세 조회에 성공했습니다.", result), HttpStatus.OK);
    }

    @PostMapping("/auth/products/orders")
    public ResponseEntity<?> orderProduct(@RequestHeader("X_User_Id") Long userId,
                                          @RequestBody OrderProductRequest orderProductRequest)  {
        productService.orderProduct(userId, orderProductRequest);
        return new ResponseEntity<>(new ResponseDto<>(200, "주문 내역을 확인하세요.", null), HttpStatus.OK);
    }

    @GetMapping("/products/stocks/{productId}")
    public ResponseEntity<?> getProductStock(@PathVariable("productId") Long productId) {
        Integer result = productService.getProductStock(productId);
        return new ResponseEntity<>(new ResponseDto<>(200, "상품 재고 조회에 성공했습니다.", result), HttpStatus.OK);
    }
}

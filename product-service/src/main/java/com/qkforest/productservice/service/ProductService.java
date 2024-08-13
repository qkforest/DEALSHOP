package com.qkforest.productservice.service;

import com.qkforest.productservice.domain.Product;
import com.qkforest.productservice.dto.response.ProductDetailResponse;
import com.qkforest.productservice.dto.response.ProductListResponse;
import com.qkforest.productservice.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public  Page<ProductListResponse> getAllProductList(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductListResponse::from);
    }

    public ProductDetailResponse getProductDetail(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.map(ProductDetailResponse::from).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product findProductByIdOrElseThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

}

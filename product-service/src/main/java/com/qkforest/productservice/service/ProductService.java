package com.qkforest.productservice.service;

import com.qkforest.commonmodule.dto.UpdateProductStockRequest;
import com.qkforest.commonmodule.dto.UpdateStockRequest;
import com.qkforest.commonmodule.exception.BusinessLogicException;
import com.qkforest.commonmodule.exception.ExceptionCode;
import com.qkforest.productservice.domain.Product;
import com.qkforest.commonmodule.dto.FeignProductDetailResponse;
import com.qkforest.productservice.domain.ProductOption;
import com.qkforest.productservice.domain.ProductStatus;
import com.qkforest.productservice.dto.request.ProductOptionAddRequest;
import com.qkforest.productservice.dto.request.ProductSaveRequest;
import com.qkforest.productservice.dto.response.ProductDetailResponse;
import com.qkforest.productservice.dto.response.ProductListResponse;
import com.qkforest.productservice.dto.response.ProductOptionResponse;
import com.qkforest.productservice.dto.response.ProductSaveResponse;
import com.qkforest.productservice.repository.ProductOptionRepository;
import com.qkforest.productservice.repository.ProductRepository;
import com.qkforest.productservice.service.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.qkforest.productservice.domain.ProductStatus.*;

@Slf4j
@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final ProductOptionRepository  productOptionRepository;
    private final KafkaProducer kafkaProducer;

    public ProductService(ProductRepository productRepository, ProductOptionRepository productOptionRepository, KafkaProducer kafkaProducer) {
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
        this.kafkaProducer = kafkaProducer;
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

    public ProductOption findProducOptiontByIdOrElseThrow(Long producOptiontId) {
        return productOptionRepository.findById(producOptiontId)
                .orElseThrow(() -> new RuntimeException("Product Option not found"));
    }

    public List<FeignProductDetailResponse> getOrderProductDetail(Set<Long> productOptionIds) {
        List<ProductOption> productOptionList = productOptionRepository.findAllByIds(productOptionIds);
        return productOptionList.stream()
                .map(ProductOptions -> new FeignProductDetailResponse(
                        ProductOptions.getProduct().getId(),
                        ProductOptions.getId(),
                        ProductOptions.getProduct().getName(),
                        ProductOptions.getName(),
                        ProductOptions.getProduct().getPrice() + ProductOptions.getAdditionalPrice(),
                        ProductOptions.getProduct().getDescription(),
                        String.valueOf(ProductOptions.getProduct().getProductStatus()),
                        ProductOptions.getProduct().getActivation_time())
                ).toList();
    }

    @Transactional
    public ProductSaveResponse saveProduct (ProductSaveRequest productSaveRequest) {
        List<ProductOptionAddRequest> productOptions = productSaveRequest.getProductOptions();

        log.debug(String.valueOf(productOptions));

        if (productOptions == null || productOptions.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.NO_PRODUCT_OPTION);
        }

        ProductStatus ProductStatus = ON_SALE;
        LocalDateTime activationTime = LocalDateTime.now();

        if(productSaveRequest.getActivation_time() != null) {
            activationTime = productSaveRequest.getActivation_time();
            ProductStatus = PREPARING;
        }

        Product product = Product.from(productSaveRequest, ProductStatus, activationTime);
        productRepository.save(product);

        // 상품 옵션 추가
        List<ProductOptionResponse> productOptionResponseList = createProductOption(product.getId(), productOptions);

        return ProductSaveResponse.from(product, productOptionResponseList);
    }

    public List<ProductOptionResponse> createProductOption(Long productId, List<ProductOptionAddRequest> productOptions) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

        List<ProductOption> productOptionList = productOptions.stream()
                .map(productOptionAddRequest -> ProductOption.from(productOptionAddRequest, product))
                .collect(Collectors.toList());

        productOptionRepository.saveAll(productOptionList);

        return ProductOptionResponse.from(productOptionList);
    }

    @Transactional
    public void checkStock(UpdateStockRequest updateStockRequest) {
        List<UpdateProductStockRequest> updateProductStockRequestList = updateStockRequest.getUpdateProductStockRequestList();
        List<ProductOption> productOptions = productOptionRepository.findAllByIds(updateProductStockRequestList.stream()
                                                                        .map(UpdateProductStockRequest::getProductOptionId)
                                                                        .collect(Collectors.toSet()));
        Map<Long, Integer> productOptionMap = updateProductStockRequestList.stream()
                .collect(Collectors.toMap(UpdateProductStockRequest::getProductOptionId, UpdateProductStockRequest::getQuantity));

        if(checkAndReduceStock(productOptions, productOptionMap)) {
            productOptionRepository.saveAll(productOptions);
        } else {
            kafkaProducer.sendFailUpdateStockRequest("product-failUpdateStock", String.valueOf(updateStockRequest.getOrderId()));
        }
    }



    private boolean checkAndReduceStock(List<ProductOption> productOptions, Map<Long, Integer> productOptionMap) {
        for (ProductOption p : productOptions) {
            int stock = productOptionMap.get(p.getId());
            if(stock > p.getStock()) {
                return false;
            }
            p.updateStock(-stock);
        }
        return true;
    }

    @Transactional
    public void restock(UpdateStockRequest updateStockRequest) {
        List<UpdateProductStockRequest> updateProductStockRequestList = updateStockRequest.getUpdateProductStockRequestList();
        List<ProductOption> productOptions = productOptionRepository.findAllByIds(updateProductStockRequestList.stream()
                .map(UpdateProductStockRequest::getProductOptionId)
                .collect(Collectors.toSet()));
        Map<Long, Integer> productOptionMap = updateProductStockRequestList.stream()
                .collect(Collectors.toMap(UpdateProductStockRequest::getProductOptionId, UpdateProductStockRequest::getQuantity));
        for (ProductOption p : productOptions) {
            int stock = productOptionMap.get(p.getId());
            p.updateStock(stock);
        }
    }
}

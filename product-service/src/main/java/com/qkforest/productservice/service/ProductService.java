package com.qkforest.productservice.service;

import com.qkforest.commonmodule.dto.product.request.OrderProductRequest;
import com.qkforest.commonmodule.dto.product.request.OrderSaveRequest;
import com.qkforest.commonmodule.dto.product.response.FeignOrderDetailResponse;
import com.qkforest.commonmodule.dto.product.response.FeignProductInfosResponse;
import com.qkforest.commonmodule.exception.BusinessLogicException;
import com.qkforest.commonmodule.exception.ExceptionCode;
import com.qkforest.productservice.domain.Product;
import com.qkforest.productservice.dto.request.ProductSaveRequest;
import com.qkforest.productservice.dto.response.ProductInfoResponse;
import com.qkforest.productservice.repository.ProductRepository;
import com.qkforest.productservice.service.event.OrderRequestEvent;
import com.qkforest.productservice.service.event.ProductEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class
ProductService {

    private final ProductRepository productRepository;
    private final ProductEventListener productEventListener;
    private final StockRedisService stockRedisService;

    public ProductService(ProductRepository productRepository, ProductEventListener productEventListener, StockRedisService stockRedisService) {
        this.productRepository = productRepository;
        this.productEventListener = productEventListener;
        this.stockRedisService = stockRedisService;
    }

    @Transactional
    public ProductInfoResponse saveProduct (ProductSaveRequest productSaveRequest) {
        // 구매 활성화 시간 입력하지 않은 경우(현재 시간부터 구매 가능)
        LocalDateTime activationTime = LocalDateTime.now();
        // 구매 활성화 사간을 입력 받은 경우 입력 받은 값으로 갱신
        if(productSaveRequest.getActivation_time() != null) {
            activationTime = productSaveRequest.getActivation_time();
        }
        Product product = Product.from(productSaveRequest, activationTime);
        productRepository.save(product);
        stockRedisService.saveProductStock(product.getId(), product.getStock());
        return ProductInfoResponse.from(product);
    }

    public Page<ProductInfoResponse> getProductList(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductInfoResponse::from);
    }

    public Product findProductByIdOrElseThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NO_SUCH_PRODUCT));
    }

    public ProductInfoResponse getProductInfo(Long productId) {
        Product product = findProductByIdOrElseThrow(productId);
        return ProductInfoResponse.from(product);
    }

    public List<FeignProductInfosResponse> getProductInfosByIds(Set<Long> productIds) {
        List<Product> productList = productRepository.findAllByIds(productIds);
        return productList.stream()
                .map(products-> new FeignProductInfosResponse(
                        products.getId(),
                        products.getTitle(),
                        products.getPrice(),
                        products.getDescription(),
                        products.getActivationTime())
                ).toList();
    }

    @Transactional
    public void orderProduct(Long userId, OrderProductRequest orderProductRequest)  {
        Long productId = orderProductRequest.getProductId();
        int quantity = orderProductRequest.getQuantity();
        Product product = checkAvailable(productId, quantity);
        reduceStock(product, quantity);
        productEventListener.onOrderRequestSuccess(new OrderRequestEvent(this, new OrderSaveRequest(userId, productId, quantity, product.getPrice(), product.getPrice() * quantity)));
    }

    private Product checkAvailable(Long productId, int quantity) {
        Product product = findProductByIdOrElseThrow(productId);
        if(product.getActivationTime().isBefore(LocalDateTime.now())) {
            Integer stock = stockRedisService.getProductStock(productId);
            if (stock == null) {
                stockRedisService.saveProductStock(productId, product.getStock());
                stock = product.getStock();
            }
            if (stock < quantity) {
                throw new BusinessLogicException(ExceptionCode.NOT_ENOUGH_STOCK);
            }
        } else {
            throw new BusinessLogicException(ExceptionCode.NOT_AVAILABLE_TIME);
        }
        return product;
    }

    private void reduceStock(Product product, int quantity) {
        try {
            stockRedisService.reduceStockWithLua(product.getId(), quantity);
        } catch (Exception e) {
            throw new RuntimeException("레디스 상품 재고 감소 예외");
        }
    }

    @Transactional
    public void restock(OrderProductRequest orderProductRequest) {
        Long productId = orderProductRequest.getProductId();
        int quantity = orderProductRequest.getQuantity();
        Integer stock = stockRedisService.getProductStock(productId);
        try{
            if (stock == null) {
                Product product = findProductByIdOrElseThrow(productId);
                stockRedisService.saveProductStock(productId, product.getStock());
            }
            stockRedisService.restockWithLua(productId, quantity);
        } catch(Exception e) {
            throw new RuntimeException("레디스 상품 재고 증가 예외");
        }
    }

    public List<FeignOrderDetailResponse> getOrderProductInfos(Map<Long, Integer> productIdsAndQuantities) {
        List<Product> productList = productRepository.findAllByIds(productIdsAndQuantities.keySet());
        return productList.stream()
                .map(products-> new FeignOrderDetailResponse(
                        products.getId(),
                        products.getTitle(),
                        products.getPrice(),
                        productIdsAndQuantities.get(products.getId()),
                        products.getDescription(),
                        products.getActivationTime())
                ).toList();
    }

    public int getProductStock(Long productId) {
        Integer productStock = stockRedisService.getProductStockWithLua(productId);
        if (productStock == null) {
            Product product = findProductByIdOrElseThrow(productId);
            productStock = product.getStock();
        }
        return productStock;
    }

}
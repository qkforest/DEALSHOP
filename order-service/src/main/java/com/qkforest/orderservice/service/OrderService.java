package com.qkforest.orderservice.service;

import com.qkforest.commonmodule.dto.FeignProductDetailResponse;
import com.qkforest.commonmodule.dto.UpdateProductStockRequest;
import com.qkforest.commonmodule.dto.UpdateStockRequest;
import com.qkforest.commonmodule.exception.BusinessLogicException;
import com.qkforest.commonmodule.exception.ExceptionCode;
import com.qkforest.orderservice.client.ProductServiceClient;
import com.qkforest.orderservice.domain.OrderDetail;
import com.qkforest.orderservice.domain.OrderStatus;
import com.qkforest.orderservice.dto.reponse.OrderDetailsResponse;
import com.qkforest.orderservice.dto.request.OrderSaveRequest;
import com.qkforest.orderservice.repository.OrderDetailRepository;
import com.qkforest.orderservice.repository.OrderRepository;
import com.qkforest.orderservice.service.kafka.KafkaProducer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.qkforest.orderservice.domain.Order;
import org.springframework.web.servlet.HandlerMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final ProductServiceClient productServiceClient;
    private final OrderDetailRepository orderDetailRepository;
    private final KafkaProducer kafkaProducer;

    public OrderService(OrderRepository orderRepository, OrderDetailService orderDetailService, ProductServiceClient productServiceClient, OrderDetailRepository orderDetailRepository, KafkaProducer kafkaProducer, HandlerMapping resourceHandlerMapping) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
        this.productServiceClient = productServiceClient;
        this.orderDetailRepository = orderDetailRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public Order findOrderByIdOrElseThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("notFoundData"));
    }

    public OrderDetailsResponse getOrderDetail(Long orderId) {
        Order order = findOrderByIdOrElseThrow(orderId);
        List<OrderDetail> orderDetailList = orderDetailService.findAllByOrderIdOrElseThrow(orderId);
        Set<Long> productOptionIds = extractProductOptionIds(orderDetailList);
        List<FeignProductDetailResponse> ProductDetails = fetchProductDetails(productOptionIds);

        return new OrderDetailsResponse(order.getTotalPrice(), order.getOrderStatus().getValue(), ProductDetails);
    }

    private Set<Long> extractProductOptionIds(List<OrderDetail> orderDetailList) {
        return orderDetailList.stream()
                .map(OrderDetail::getProductOptionId)
                .collect(Collectors.toSet());
    }

    private List<FeignProductDetailResponse> fetchProductDetails(Set<Long> productOptionIds) {
        return productServiceClient.getProductDetailsByIds(productOptionIds).getBody().getData();
    }

    @Transactional
    public Long saveOrder(Long userId, OrderSaveRequest orderSaveRequest) {
        // 주문할 상품 리스트
        List<UpdateProductStockRequest> updateProductStockRequestList = orderSaveRequest.getOrderProductRequestList();

        System.out.println(orderSaveRequest);

        // 주문 생성
        Order order = orderRepository.save(Order.builder().userId(userId).orderStatus(OrderStatus.NEW_ORDER).totalPrice(0L).build());

        // 주문 상세 생성
        List<OrderDetail> orderProductList = createOrderProducts(updateProductStockRequestList, order);
        orderDetailRepository.saveAll(orderProductList);

        // 주문 총액 반영
        Long totalPrice = calculateTotalPrice(orderProductList);
        order.updateTotalPrice(totalPrice);

        // 주문 후 재고 감소
        UpdateStockRequest reduceStockRequest = new UpdateStockRequest(1, order.getId(), updateProductStockRequestList);
        updateStock(reduceStockRequest);

        return order.getId();

    }

    private void updateStock(UpdateStockRequest updateStockRequest) {
        kafkaProducer.sendUpdateStockRequest("order-updateStock", updateStockRequest);
    }


    private List<OrderDetail> createOrderProducts(List<UpdateProductStockRequest> updateProductStockRequestList, Order order) {

        Set<Long> productOptionIds = updateProductStockRequestList.stream()
                .map(UpdateProductStockRequest::getProductOptionId)
                .collect(Collectors.toSet());

        List<FeignProductDetailResponse> productDetailsList = fetchProductDetails(productOptionIds);

        return updateProductStockRequestList.stream()
                .map(UpdateProductStockRequest -> createOrderDetail(UpdateProductStockRequest, productDetailsList, order))
                .collect(Collectors.toList());
    }

    private OrderDetail createOrderDetail(UpdateProductStockRequest updateProductStockRequest,
                                          List<FeignProductDetailResponse> productDetailsList,
                                          Order order) {
        FeignProductDetailResponse productDetails = productDetailsList.stream()
                .filter(p -> p.getProduct_id().equals(updateProductStockRequest.getProductId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("notFoundData"));

        if (productDetails.getActivation_time() != null && productDetails.getActivation_time().isAfter(LocalDateTime.now())) {
            order.updateOrderStatus(OrderStatus.FAILED);
            throw new RuntimeException("fail order");
        }

        int quantity = updateProductStockRequest.getQuantity();
        return OrderDetail.builder()
                .order(order)
                .productId(productDetails.getProduct_id())
                .productOptionId(updateProductStockRequest.getProductOptionId())
                .quantity(quantity)
                .unitPrice(productDetails.getPrice())
                .build();
    }

    private Long calculateTotalPrice(List<OrderDetail> orderProducts) {
        return orderProducts.stream()
                .mapToLong(orderDetail -> orderDetail.getUnitPrice() * orderDetail.getQuantity() )
                .sum();
    }

    @Transactional
    public void handleFailUpdateStock(Long orderId) {
        Order order = findOrderByIdOrElseThrow(orderId);
        order.updateOrderStatus(OrderStatus.FAILED);
    }

    public void updateOrderStatusToInTransit() {
        updateOrderStatus(
            findAllBetweenYesterdayAndToday().stream().toList(),
                OrderStatus.IN_TRANSIT
        );
    }

    public void updateOrderStatusToShipped() {
        updateOrderStatus(
                findAllBetweenTwoDaysAgoAndYesterday().stream().toList(),
                OrderStatus.SHIPPED
        );
    }

    private void updateOrderStatus(List<Order> orderList, OrderStatus orderStatus) {
        orderList.forEach(order -> order.updateOrderStatus(orderStatus));
    }

    public List<Order> findAllBetweenYesterdayAndToday() {
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime yesterday = today.minusDays(1);

        return orderRepository.findAllBetweenPrevDayAndToday(yesterday, today);
    }

    public List<Order> findAllBetweenTwoDaysAgoAndYesterday() {
        LocalDateTime yesterday = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime twoDaysAgo = yesterday.minusDays(1);

        return orderRepository.findAllBetweenPrevDayAndToday(twoDaysAgo, yesterday);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = findOrderByIdOrElseThrow(orderId);
        if(order.getOrderStatus() == OrderStatus.NEW_ORDER) {
            restock(order);
            order.updateOrderStatus(OrderStatus.CANCELLED);
        }
        else {
            throw new BusinessLogicException(ExceptionCode.CANCEL_NOT_APPROVED);
        }
    }

    @Transactional
    public void returnProducts(Long orderId) {
        Order order = findOrderByIdOrElseThrow(orderId);
        if(order.getOrderStatus() == OrderStatus.SHIPPED) {
            restock(order);
            order.updateOrderStatus(OrderStatus.RETURNED);
        }
        else {
            throw new BusinessLogicException(ExceptionCode.RETURN_NOT_APPROVED);
        }
    }

    private void restock(Order order) {
        List<OrderDetail> orderDetailList = orderDetailService.findAllByOrderIdOrElseThrow(order.getId());

        List<UpdateProductStockRequest> updateProductStockRequestList = orderDetailList.stream()
                .map(orderDetail -> new UpdateProductStockRequest(orderDetail.getProductId(), orderDetail.getProductOptionId(), orderDetail.getQuantity()))
                .collect(Collectors.toList());

        UpdateStockRequest restockRequest = new UpdateStockRequest(2, order.getId(), updateProductStockRequestList);
        updateStock(restockRequest);
    }
}

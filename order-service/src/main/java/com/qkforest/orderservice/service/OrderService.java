package com.qkforest.orderservice.service;

import com.qkforest.commonmodule.dto.order.OrderCancelRequest;
import com.qkforest.commonmodule.dto.product.request.OrderProductRequest;
import com.qkforest.commonmodule.dto.product.response.FeignOrderDetailResponse;
import com.qkforest.commonmodule.exception.BusinessLogicException;
import com.qkforest.commonmodule.exception.ExceptionCode;
import com.qkforest.orderservice.client.ProductServiceClient;
import com.qkforest.orderservice.domain.Order;
import com.qkforest.orderservice.domain.OrderDetail;
import com.qkforest.orderservice.domain.OrderStatus;
import com.qkforest.orderservice.dto.reponse.OrderDetailsResponse;
import com.qkforest.orderservice.repository.OrderRepository;
import com.qkforest.orderservice.service.event.OrderCancelEvent;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final ProductServiceClient productServiceClient;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderService(OrderRepository orderRepository, OrderDetailService orderDetailService, ProductServiceClient productServiceClient, ApplicationEventPublisher applicationEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
        this.productServiceClient = productServiceClient;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public Order findOrderByIdOrElseThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NO_SUCH_ORDER));
    }

    public OrderDetailsResponse getOrderDetails(Long userId, Long orderId) {
        Order order = findOrderByIdOrElseThrow(orderId);
        if(order.getUserId().equals(userId)) {
            return getOrderDetail(order);
        } else {
            throw new BusinessLogicException(ExceptionCode.NOT_AVAILABLE_USER);
        }
    }

    public OrderDetailsResponse getOrderDetail(Order order) {
        Long orderId = order.getId();
        List<OrderDetail> orderDetailList = orderDetailService.findAllByOrderIdOrElseThrow(orderId);
        Map<Long, Integer> productIdsAndQuantities = extractProductIdsAndQuantities(orderDetailList);
        List<FeignOrderDetailResponse> orderDetails = fetchOrderDetails(productIdsAndQuantities);

        return new OrderDetailsResponse(orderId, order.getTotalPrice(), order.getOrderStatus().getValue(), order.getCreatedDate(), orderDetails);
    }

    public List<OrderDetailsResponse> getOrderList(Long userId) {
        List<Order> orderList = orderRepository.findOrderListByUserId(userId);
        List<OrderDetailsResponse> orderDetailsResponseList = new ArrayList<>();
        orderList.forEach(order -> orderDetailsResponseList.add(getOrderDetail(order)));
        return orderDetailsResponseList;
    }

    private Map<Long, Integer> extractProductIdsAndQuantities(List<OrderDetail> orderDetailList) {
        return orderDetailList.stream()
                .collect(Collectors.toMap(OrderDetail::getProductId, OrderDetail::getQuantity));
    }

    private List<FeignOrderDetailResponse> fetchOrderDetails(Map<Long, Integer> productIdsAndQuantities) {
        return productServiceClient.getOrderDetailsByIdsAndQuantities(productIdsAndQuantities).getBody().getData();
    }

    @Transactional
    public void updateOrderStatusToCancel(Long orderId) {
        Order order = findOrderByIdOrElseThrow(orderId);
        order.updateOrderStatus(OrderStatus.FAILED);
    }

    @Transactional
    public void updateOrderStatusToInTransit() {
        updateOrderStatus(
            findAllBetweenYesterdayAndToday().stream().toList(),
                OrderStatus.IN_TRANSIT
        );
    }

    @Transactional
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
    public void cancelOrder2(Long userId, Long orderId, OrderStatus previous, OrderStatus update, ExceptionCode exceptionCode) {
        Order order = findOrderByIdOrElseThrow(orderId);
        if(order.getUserId().equals(userId)) {
            if(order.getOrderStatus() == previous) {
                order.updateOrderStatus(update);
                List<OrderDetail> orderDetailList = orderDetailService.findAllByOrderIdOrElseThrow(order.getId());
                List<OrderProductRequest> OrderProductList = orderDetailList.stream()
                        .map(orderDetail -> new OrderProductRequest(orderDetail.getProductId(), orderDetail.getQuantity()))
                        .toList();
                applicationEventPublisher.publishEvent(new OrderCancelEvent(this, new OrderCancelRequest(orderId, OrderProductList)));
            }
            else {
                throw new BusinessLogicException(exceptionCode);
            }
        } else {
            throw new BusinessLogicException(ExceptionCode.NOT_AVAILABLE_USER);
        }
    }

    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order order = findOrderByIdOrElseThrow(orderId);
        if(order.getUserId().equals(userId)) {
            if(order.getOrderStatus() == OrderStatus.NEW_ORDER) {
                order.updateOrderStatus(OrderStatus.CANCELLED);

            }
            else {
                throw new BusinessLogicException(ExceptionCode.CANCEL_NOT_APPROVED);
            }
        } else {
            throw new BusinessLogicException(ExceptionCode.NOT_AVAILABLE_USER);
        }
    }

    @Transactional
    public void returnProducts(Long userId, Long orderId) {
        Order order = findOrderByIdOrElseThrow(orderId);
        if(order.getUserId().equals(userId)) {
            if (order.getOrderStatus() == OrderStatus.SHIPPED) {
                order.updateOrderStatus(OrderStatus.RETURNED);

            } else {
                throw new BusinessLogicException(ExceptionCode.RETURN_NOT_APPROVED);
            }
        } else {
            throw new BusinessLogicException(ExceptionCode.NOT_AVAILABLE_USER);
        }
    }
}

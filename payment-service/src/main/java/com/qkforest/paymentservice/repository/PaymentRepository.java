package com.qkforest.paymentservice.repository;

import com.qkforest.paymentservice.domain.Payment;
import com.qkforest.paymentservice.domain.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Payment p SET p.paymentStatus = :paymentStatus WHERE p.orderId = :orderId")
    void updatePaymentStatusByOrderId(@Param("orderId") Long orderId, @Param("paymentStatus") PaymentStatus paymentStatus);
}

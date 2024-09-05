package com.qkforest.productservice.repository;

import com.qkforest.productservice.domain.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :productId")
    Product findByIdWithPessimisticLock(Long productId);

    @Query("select p from Product p where p.id in :productIds")
    List<Product> findAllByIds(Set<Long> productIds);

}

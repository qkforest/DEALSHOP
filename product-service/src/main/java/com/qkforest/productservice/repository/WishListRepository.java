package com.qkforest.productservice.repository;

import com.qkforest.productservice.domain.WishList;
import com.qkforest.productservice.dto.response.WishListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long>  {
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    Optional<WishList> findByIdAndUserId(Long wishListId, Long userId);
    Page<WishListResponse> findAllByUserId(Long userId, Pageable pageable);
}

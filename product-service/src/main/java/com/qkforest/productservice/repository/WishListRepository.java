package com.qkforest.productservice.repository;

import com.qkforest.productservice.domain.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long>, WishListRepositoryCustom {
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    Optional<WishList> findByIdAndUserId(Long wishListId, Long userId);
}

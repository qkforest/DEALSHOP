package com.qkforest.productservice.repository;

import com.qkforest.productservice.dto.response.WishListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishListRepositoryCustom {
    Page<WishListResponse> findAllByUserId(Long userId, Pageable pageable);
}

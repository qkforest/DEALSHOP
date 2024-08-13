package com.qkforest.productservice.service;

import com.qkforest.productservice.domain.Product;
import com.qkforest.productservice.domain.WishList;
import com.qkforest.productservice.dto.request.WishListAddRequest;
import com.qkforest.productservice.dto.response.WishListResponse;
import com.qkforest.productservice.dto.response.WishListUpdateResponse;
import com.qkforest.productservice.exception.BusinessLogicException;
import com.qkforest.productservice.exception.ExceptionCode;
import com.qkforest.productservice.repository.WishListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListService {

    private final ProductService productService;
    private final WishListRepository wishListRepository;

    public WishListService(ProductService productService, WishListRepository wishListRepository) {
        this.productService = productService;
        this.wishListRepository = wishListRepository;
    }
    @Transactional
    public Long addWishList(Long userId, WishListAddRequest wishListAddRequest) {
        if (wishListRepository.existsByUserIdAndProductId(userId, wishListAddRequest.getProductId())) {
            throw new BusinessLogicException(ExceptionCode.WISHLIST_EXISTS);
        }
        Product product = productService.findProductByIdOrElseThrow(wishListAddRequest.getProductId());
        WishList wishList = wishListRepository.save(WishList.builder()
                                                            .product(product)
                                                            .userId(userId)
                                                            .quantity(wishListAddRequest.getQuantity())
                                                            .build());
        return wishList.getId();
    }

    public Page<WishListResponse> getWishList(Long userId, Pageable pageable) {
        return wishListRepository.findAllByUserId(userId, pageable);
    }

    @Transactional
    public WishListUpdateResponse updateQuantity(Long userId, Long wishListId, int quantity) {
        WishList wishList = wishListRepository.findByIdAndUserId(wishListId, userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.INVALID_UPDATE_REQUEST));
        wishList.updateQuantity(quantity);
        return new WishListUpdateResponse(wishList.getId(), wishList.getProduct().getId(), quantity);
    }

    @Transactional
    public void deleteWishList(Long userId, Long wishListId) {
        WishList wishlist = wishListRepository.findByIdAndUserId(wishListId, userId)
                .orElseThrow(() ->new BusinessLogicException(ExceptionCode.NOT_FOUND_WISHLIST));
        wishListRepository.delete(wishlist);
    }

}

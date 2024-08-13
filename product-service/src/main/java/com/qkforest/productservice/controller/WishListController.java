package com.qkforest.productservice.controller;

import com.qkforest.productservice.dto.request.WishListAddRequest;
import com.qkforest.productservice.dto.request.WishListUpdateRequest;
import com.qkforest.productservice.dto.response.ResponseDto;
import com.qkforest.productservice.dto.response.WishListResponse;
import com.qkforest.productservice.dto.response.WishListUpdateResponse;
import com.qkforest.productservice.service.WishListService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> addWishList(@PathVariable("userId") Long userId,
                                         @RequestBody @Valid WishListAddRequest wishListAddRequest,
                                         BindingResult bindingResult) {
        Long wishProductId  = wishListService.addWishList(userId, wishListAddRequest);
        return new ResponseEntity<>(new ResponseDto<>(1, "위시리스트에 상품이 등록되었습니다.", wishProductId), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getWishList(@PathVariable("userId") Long userId,
                                         Pageable pageable) {
        Page<WishListResponse> result = wishListService.getWishList(userId, pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "위시리스트 목록 조회에 성공하였습니다.", result), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/{wishListId}")
    public ResponseEntity<?> updateQuantityInWishList(@PathVariable("userId") Long userId,
                                                      @PathVariable("wishListId") Long wishListId,
                                                      @RequestBody @Valid WishListUpdateRequest wishListUpdateRequest,
                                                      BindingResult bindingResult) {
        WishListUpdateResponse result = wishListService.updateQuantity(userId, wishListId, wishListUpdateRequest.getQuantity());
        return new ResponseEntity<>(new ResponseDto<>(1, "위시리스트 상품 수량 변경에 성공하였습니다.", result), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/{wishListId}")
    public ResponseEntity<?> deleteWishList(@PathVariable("userId") Long userId,
                                            @PathVariable("wishListId") Long wishListId) {
        wishListService.deleteWishList(userId, wishListId);
        return new ResponseEntity<>(new ResponseDto<>(1, "위시리스트 상품 삭제에 성공하였습니다.", null), HttpStatus.OK);
    }
}

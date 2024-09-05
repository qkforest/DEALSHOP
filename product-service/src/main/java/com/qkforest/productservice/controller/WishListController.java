package com.qkforest.productservice.controller;

import com.qkforest.commonmodule.dto.ResponseDto;
import com.qkforest.productservice.dto.request.WishListAddRequest;
import com.qkforest.productservice.dto.request.WishListUpdateRequest;
import com.qkforest.productservice.dto.response.WishListResponse;
import com.qkforest.productservice.service.WishListService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/wishlists")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PostMapping("")
    public ResponseEntity<?> addWishList(@RequestHeader("X_User_Id") Long userId,
                                         @RequestBody @Valid WishListAddRequest wishListAddRequest) {
        Long wishProductId  = wishListService.addWishList(userId, wishListAddRequest);
        return new ResponseEntity<>(new ResponseDto<>(200, "위시리스트에 상품이 등록되었습니다.", wishProductId), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getWishList(@RequestHeader("X_User_Id") Long userId,
                                         Pageable pageable) {
        Page<WishListResponse> result = wishListService.getWishList(userId, pageable);
        return new ResponseEntity<>(new ResponseDto<>(200, "위시리스트 목록 조회에 성공하였습니다.", result), HttpStatus.OK);
    }

    @PatchMapping("/{wishListId}")
    public ResponseEntity<?> updateQuantityInWishList(@RequestHeader("X_User_Id") Long userId,
                                                      @PathVariable("wishListId") Long wishListId,
                                                      @RequestBody @Valid WishListUpdateRequest wishListUpdateRequest) {
        WishListResponse result = wishListService.updateQuantity(userId, wishListId, wishListUpdateRequest.getQuantity());
        return new ResponseEntity<>(new ResponseDto<>(200, "위시리스트 상품 수량 변경에 성공하였습니다.", result), HttpStatus.OK);
    }

    @DeleteMapping("/{wishListId}")
    public ResponseEntity<?> deleteWishList(@RequestHeader("X_User_Id") Long userId,
                                            @PathVariable("wishListId") Long wishListId) {
        wishListService.deleteWishList(userId, wishListId);
        return new ResponseEntity<>(new ResponseDto<>(200, "위시리스트 상품 삭제에 성공하였습니다.", null), HttpStatus.OK);
    }
}

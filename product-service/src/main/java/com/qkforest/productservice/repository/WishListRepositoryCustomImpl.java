package com.qkforest.productservice.repository;

import com.qkforest.productservice.dto.response.WishListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.qkforest.productservice.domain.QProduct.product;
import static com.qkforest.productservice.domain.QWishList.wishList;

public class WishListRepositoryCustomImpl implements WishListRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public WishListRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<WishListResponse> findAllByUserId(Long userId, Pageable pageable) {
        List<WishListResponse> content = queryFactory
                .select(Projections.constructor(WishListResponse.class,
                        wishList.id,
                        wishList.product.id,
                        wishList.product.name,
                        wishList.quantity
                ))
                .from(wishList)
                .join(wishList.product, product)
                .where(wishList.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(wishList.count())
                .from(wishList);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}

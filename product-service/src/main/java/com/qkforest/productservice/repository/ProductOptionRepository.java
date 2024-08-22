package com.qkforest.productservice.repository;

import com.qkforest.productservice.domain.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    @Query("select p from ProductOption p where p.id in :productOptionIds")
    List<ProductOption> findAllByIds(@Param("productOptionIds") Set<Long> productOptionIds);
}

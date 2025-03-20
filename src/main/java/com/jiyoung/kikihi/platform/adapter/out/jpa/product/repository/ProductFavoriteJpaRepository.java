package com.jiyoung.kikihi.platform.adapter.out.jpa.product.repository;

import com.jiyoung.kikihi.platform.adapter.out.jpa.product.ProductFavoriteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFavoriteJpaRepository extends JpaRepository<ProductFavoriteJpaEntity, Long> {

}

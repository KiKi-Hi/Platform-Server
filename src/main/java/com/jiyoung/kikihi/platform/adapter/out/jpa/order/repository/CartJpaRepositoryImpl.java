package com.jiyoung.kikihi.platform.adapter.out.jpa.order.repository;

import com.jiyoung.kikihi.platform.adapter.out.jpa.order.CartJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.order.QCartJpaEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CartJpaRepositoryImpl implements CartJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findProductIdsByUserId(UUID userId) {
        QCartJpaEntity cart = QCartJpaEntity.cartJpaEntity;
        return queryFactory
                .select(cart.productId)
                .from(cart)
                .where(cart.userId.eq(userId))
                .fetch();
    }

    @Override
    public void deleteByProductId(String productId, UUID userId) {
        QCartJpaEntity cart = QCartJpaEntity.cartJpaEntity;
        queryFactory
                .delete(cart)
                .where(cart.productId.eq(productId)
                        .and(cart.userId.eq(userId)))
                .execute();

    }

    @Override
    public Optional<CartJpaEntity> findByProductIdAndUserId(String productId, UUID userId) {
        QCartJpaEntity cart = QCartJpaEntity.cartJpaEntity;
        return Optional.ofNullable(
                queryFactory
                        .select(cart)
                        .from(cart)
                        .where(cart.productId.eq(productId)
                                .and(cart.userId.eq(userId)))
                        .fetchOne()
        );
    }
}

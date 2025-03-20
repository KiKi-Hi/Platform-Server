package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.product.repository.ProductFavoriteJpaRepository;
import com.jiyoung.kikihi.platform.application.port.out.product.ProductReactionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductReactionPersistenceAdapter implements ProductReactionPort {

    private final ProductFavoriteJpaRepository repository;

    @Override
    public void saveLikeProductReaction(Long productId, Long userId) {

    }

    @Override
    public boolean checkProductLikeReaction(Long productId, Long memberId) {
        return false;
    }

    @Override
    public Long loadProductLikeCount(Long productId) {
        return 0L;
    }
}

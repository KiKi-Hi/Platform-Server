package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.product.ReactionJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.product.repository.ReactionJpaRepository;
import com.jiyoung.kikihi.platform.application.port.out.product.ReactionPort;
import com.jiyoung.kikihi.platform.domain.product.LikeList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductReactionPersistenceAdapter implements ReactionPort {


    private final ReactionJpaRepository reactionJpaRepository;


    @Override
    public Optional<LikeList> checkLike(Long productId, Long userId) {
        if (productId == null || userId == null) {
            throw new IllegalArgumentException("Product ID and User ID must not be null");
        }

        return reactionJpaRepository.findByProductIdAndUserId(productId, userId)
                .map(entity -> {
                    if (entity.getId() == null) {
                        throw new IllegalStateException("Entity ID must not be null");
                    }
                    return entity.toDomain();
                });
    }
    @Override
    public void saveLikeReaction(Long productId, Long userId) {
        reactionJpaRepository.saveByProductIdAndUserId(productId,userId);
    }

    @Override
    public void deleteLikeReaction(Long productId, Long userId) {
        reactionJpaRepository.deleteByProductIdAndUserId(productId,userId);
    }

    @Override
    public Long loadProductLikeCount(Long productId) {
        return 0L;
    }
}

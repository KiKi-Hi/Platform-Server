package com.jiyoung.kikihi.platform.application;

import com.jiyoung.kikihi.platform.application.port.in.product.ReactionPartsUseCase;
import com.jiyoung.kikihi.platform.application.port.out.product.ProductReactionPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomReactionService implements ReactionPartsUseCase {

    private final ProductReactionPort reactionPort;

    @Override
    public void addLike(Long productId, Long userId) {

    }

    @Override
    public void RemoveLike(Long productId, Long userId) {

    }
}

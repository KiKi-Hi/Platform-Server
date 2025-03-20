package com.jiyoung.kikihi.platform.application;

import com.jiyoung.kikihi.platform.application.port.in.product.ReactionPartsUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomReactionService implements ReactionPartsUseCase {


    @Override
    public void addLike(Long productId, Long userId) {

    }

    @Override
    public void RemoveLike(Long productId, Long userId) {

    }
}

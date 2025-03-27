package com.jiyoung.kikihi.platform.application.port.service;

import com.jiyoung.kikihi.platform.application.port.in.product.ReactionUseCase;
import com.jiyoung.kikihi.platform.application.port.out.product.LoadProductPort;
import com.jiyoung.kikihi.platform.application.port.out.product.ReactionPort;
import com.jiyoung.kikihi.platform.domain.product.LikeList;
import com.jiyoung.kikihi.platform.domain.product.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReactionService implements ReactionUseCase {

    private final ReactionPort reactionPort;

    @Override
    public boolean handleLike(Long productId, Long userId) {
        // 상품 검색, 없으면 null
        Optional<LikeList> loadProduct=reactionPort.checkLike(productId, userId);
        System.out.println(loadProduct);

        if(loadProduct.isPresent()){
            reactionPort.deleteLikeReaction(loadProduct.get().getProductId(),loadProduct.get().getUserId());
            return false;

        }else{
            reactionPort.saveLikeReaction(productId, userId);
            return true;
        }
    }
}

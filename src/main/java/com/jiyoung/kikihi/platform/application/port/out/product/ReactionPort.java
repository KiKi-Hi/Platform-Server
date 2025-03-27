package com.jiyoung.kikihi.platform.application.port.out.product;

import com.jiyoung.kikihi.platform.domain.product.LikeList;

import java.util.Optional;

public interface ReactionPort {

    // 이미 좋아요가 되어있는지 확인
    Optional<LikeList> checkLike(Long productId, Long userId);

    // 좋아요를 저장한다
    void saveLikeReaction(Long productId, Long userId);

    //좋아요를 취소한다
    void deleteLikeReaction(Long productId, Long userId);

    // 좋아요 개수 가져오기
    Long loadProductLikeCount(Long productId);

}

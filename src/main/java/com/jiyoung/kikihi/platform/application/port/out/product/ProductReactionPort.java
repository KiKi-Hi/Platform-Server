package com.jiyoung.kikihi.platform.application.port.out.product;

public interface ProductReactionPort {

    // 좋아요를 저장한다
    void saveLikeProductReaction(Long productId, Long userId);

    // 특정 상품에 좋아요 리액션을 남긴게 있는지 확인
    boolean checkProductLikeReaction(Long productId, Long memberId);

    // 좋아요 개수 가져오기
    Long loadProductLikeCount(Long productId);

}

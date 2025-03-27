package com.jiyoung.kikihi.platform.application.port.in.product;

public interface ReactionUseCase {

    /*
        상품에 대해서 리액션을 한다.
        한번에 처리한다.
     */

    boolean handleLike(Long productId,Long userId);
}

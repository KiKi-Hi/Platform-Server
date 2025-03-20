package com.jiyoung.kikihi.platform.application.port.in.product;

public interface ReactionPartsUseCase {

    /*
        상품에 대해서 리액션을 한다.
        한번에 처리한다.
     */

    void addLike(Long productId, Long userId);

    void RemoveLike(Long productId, Long userId);


}

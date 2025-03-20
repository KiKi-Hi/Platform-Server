package com.jiyoung.kikihi.platform.application.port.in.product;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.CustomRequest;
import com.jiyoung.kikihi.platform.domain.product.Custom;

public interface CreateCustomUseCase {

    /*
        - 상품의 조합을 진행한다.
        - 조합 유효성 검사는 서비스 계층에서 수행한다. (필수 부품 존재 여부)
        -
        - 조합을 저장
     */

    // 상품 생성
    Custom create(CustomRequest request);

    // 장바구니에 넣도록 한다.
    void addCart(Custom custom, Long userId);


}

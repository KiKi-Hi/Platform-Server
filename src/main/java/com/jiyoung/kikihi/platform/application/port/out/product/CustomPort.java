package com.jiyoung.kikihi.platform.application.port.out.product;

import com.jiyoung.kikihi.platform.domain.product.CustomKeyboard;

public interface CustomPort {
    // 커스텀한 상품 저장하기
    CustomKeyboard saveCustom(CustomKeyboard custom);
}

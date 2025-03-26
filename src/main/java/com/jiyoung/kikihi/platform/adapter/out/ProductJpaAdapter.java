package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.product.CustomKeyBoardJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.product.repository.CustomKeyBoardJpaRepository;
import com.jiyoung.kikihi.platform.application.port.out.product.CustomPort;
import com.jiyoung.kikihi.platform.domain.product.CustomKeyboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductJpaAdapter implements CustomPort {

    private final CustomKeyBoardJpaRepository customRepository;

    // CustomPort
    @Override
    public CustomKeyboard saveCustom(CustomKeyboard custom) {
        CustomKeyBoardJpaEntity jpaEntity=customRepository.save(CustomKeyBoardJpaEntity.from(custom));
        return jpaEntity.toDomain();
    }
}


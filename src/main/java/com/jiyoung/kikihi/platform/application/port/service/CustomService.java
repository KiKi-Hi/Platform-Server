package com.jiyoung.kikihi.platform.application.port.service;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.CustomRequest;
import com.jiyoung.kikihi.platform.application.port.in.product.CreateCustomUseCase;
import com.jiyoung.kikihi.platform.application.port.out.product.CustomPort;
import com.jiyoung.kikihi.platform.domain.product.CustomKeyboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomService implements CreateCustomUseCase {

    private final CustomPort customPort;

    @Override
    public CustomKeyboard create(CustomRequest request) {
        CustomKeyboard cus=customPort.saveCustom(CustomKeyboard.of(request));
        return cus;
    }

    @Override
    public void addCart(CustomKeyboard custom, Long userId) {

    }
}

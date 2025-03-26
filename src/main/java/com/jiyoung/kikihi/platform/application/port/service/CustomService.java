package com.jiyoung.kikihi.platform.application.port.service;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.CustomRequest;
import com.jiyoung.kikihi.platform.application.port.in.product.CreateCustomUseCase;
import com.jiyoung.kikihi.platform.application.port.out.product.CustomPort;
import com.jiyoung.kikihi.platform.domain.product.CustomKeyboard;
import org.springframework.stereotype.Service;

@Service
public class CustomService implements CreateCustomUseCase {
    private CustomPort customPort;

    @Override
    public CustomKeyboard create(CustomRequest request) {
        CustomKeyboard cus=customPort.saveCustom(CustomKeyboard.of(request));
        return cus;
    }

    @Override
    public void addCart(CustomKeyboard custom, Long userId) {

    }
}

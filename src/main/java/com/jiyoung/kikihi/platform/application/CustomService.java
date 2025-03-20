package com.jiyoung.kikihi.platform.application;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.CustomRequest;
import com.jiyoung.kikihi.platform.application.port.in.product.*;
import com.jiyoung.kikihi.platform.domain.product.Custom;
import com.jiyoung.kikihi.platform.domain.product.Housing;
import com.jiyoung.kikihi.platform.domain.product.KeyCap;
import com.jiyoung.kikihi.platform.domain.product.Switch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomService implements CreateCustomUseCase, GetPartsUseCase, ShareCustomUseCase {



    @Override
    public Custom create(CustomRequest request) {
        return null;
    }


    @Override
    public Housing getHousing(Long id) {
        return null;
    }

    @Override
    public Page<Housing> getHousings(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Housing> getHousingsByLike(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Housing> getHousingsByCondition(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Housing> getHousingsByWishList(Pageable pageable) {
        return null;
    }

    // 키캡 상세 조회
    @Override
    public KeyCap getKeyCap(Long id) {
        return null;
    }

    @Override
    public Page<KeyCap> getKeyCaps(Pageable pageable) {
        return null;
    }

    @Override
    public Page<KeyCap> getKeyCapsByLike(Pageable pageable) {
        return null;
    }

    @Override
    public Page<KeyCap> getKeyCapsByCondition(Pageable pageable) {
        return null;
    }

    @Override
    public Page<KeyCap> getKeyCapsByWishList(Pageable pageable) {
        return null;
    }

    // 스위치 상세 조회
    @Override
    public Switch getSwitch(Long id) {
        return null;
    }

    @Override
    public Page<Switch> getSwitches(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Switch> getSwitchesByLike(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Switch> getSwitchesByCondition(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Switch> getSwitchesByWishList(Pageable pageable) {
        return null;
    }
}

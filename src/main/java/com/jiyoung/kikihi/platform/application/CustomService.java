package com.jiyoung.kikihi.platform.application;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.CustomRequest;
import com.jiyoung.kikihi.platform.application.port.in.product.*;
import com.jiyoung.kikihi.platform.application.port.out.product.DeleteProductPort;
import com.jiyoung.kikihi.platform.application.port.out.product.LoadProductPort;
import com.jiyoung.kikihi.platform.application.port.out.product.SaveProductPort;
import com.jiyoung.kikihi.platform.domain.product.Custom;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomService implements CreateCustomUseCase, GetPartsUseCase, ShareCustomUseCase {

    private final SaveProductPort savePort;
    private final LoadProductPort loadPort;
    private final DeleteProductPort deletePort;

    @Override
    public Custom create(CustomRequest request) {
        return null;
    }

    @Override
    public void addCart(Custom custom, Long userId) {

    }

    @Override
    public Product getProduct(Long id) {
        return null;
    }

    @Override
    public Page<Product> getProducts(Pageable pageable, Long categoryId) {
        return null;
    }

    @Override
    public Page<Product> getProductsByLike(Pageable pageable, Long categoryId) {
        return null;
    }

    @Override
    public Page<Product> getProductsByCondition(Pageable pageable, Long categoryId) {
        return null;
    }

    @Override
    public Page<Product> getProductsByWishList(Pageable pageable, Long categoryId) {
        return null;
    }
}

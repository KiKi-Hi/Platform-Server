package com.jiyoung.kikihi.platform.application.port.service;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.FrameFilter;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.KeycapFilter;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.SwitchFilter;
import com.jiyoung.kikihi.platform.application.port.in.product.CreateCustomUseCase;
import com.jiyoung.kikihi.platform.application.port.in.product.GetProductUseCase;
import com.jiyoung.kikihi.platform.application.port.out.product.DeleteProductPort;
import com.jiyoung.kikihi.platform.application.port.out.product.LoadProductPort;
import com.jiyoung.kikihi.platform.application.port.out.product.ProductReactionPort;
import com.jiyoung.kikihi.platform.application.port.out.product.SaveProductPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import com.jiyoung.kikihi.platform.domain.product.frame.Frame;
import com.jiyoung.kikihi.platform.domain.product.keycap.Keycap;
import com.jiyoung.kikihi.platform.domain.product.switches.Switch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductService implements GetProductUseCase {

    private final SaveProductPort savePort;
    private final LoadProductPort loadPort;
    private final ProductReactionPort reactionPort;
    private final DeleteProductPort deletePort;


    @Override
    public Product getProduct(Long id) {
        return null;
    }

    @Override
    public Page<Product> getProducts(Long categoryId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Product> getProductsByLike(Long categoryId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Product> getProductsByCondition(Long categoryId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Product> getProductsByWishList(Long categoryId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Frame> getFrameByFilter(FrameFilter filter, Pageable pageable) {
        // port를 사용해서 조회하기
        return loadPort.findAllFrames(pageable);
    }

    @Override
    public Page<Switch> getSwitchByFilter(SwitchFilter filter, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Keycap> getKeycapByFilter(KeycapFilter filter, Pageable pageable) {
        return null;
    }
}

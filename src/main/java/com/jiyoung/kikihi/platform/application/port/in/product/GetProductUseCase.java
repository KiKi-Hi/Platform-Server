package com.jiyoung.kikihi.platform.application.port.in.product;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.BaseFilter;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.FrameFilter;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.KeycapFilter;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.SwitchFilter;
import com.jiyoung.kikihi.platform.domain.product.Product;
import com.jiyoung.kikihi.platform.domain.product.frame.Frame;
import com.jiyoung.kikihi.platform.domain.product.keycap.Keycap;
import com.jiyoung.kikihi.platform.domain.product.switches.Switch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetProductUseCase {

    /*
        사용자가 키보드에 대한 정보를 얻는 유즈케이스
        관심 목록이 제일 위에 가도록 설정한다.
        필터링은 가격, 브랜드 , 색상으로 가능함
        각각 조립품(하우징, 키캡, 스위치 등)은 각 사이트의 구매 링크를 모아서 사용자가 한번에 결제하도록 만들 수 있음
     */

    // 상품 상세 조회
    Product getProduct(Long id);

    // 상품 전체 조회
    Page<Product> getProducts(Long categoryId, Pageable pageable);

    // 상품 인기순 (정렬)
    Page<Product> getProductsByLike(Long categoryId, Pageable pageable);

    // 상품 조건별 , 조건문 추가해야 한다. (정렬)
    Page<Product> getProductsByCondition(Long categoryId, Pageable pageable);

    // 상품 관심목록 조회
    Page<Product> getProductsByWishList(Long categoryId, Pageable pageable);

    // frame filter 적용
    Page<Frame> getFrameByFilter(FrameFilter filter, Pageable pageable);

    // switch filter 적용
    Page<Switch> getSwitchByFilter(SwitchFilter filter, Pageable pageable);

    // keycap filter 적용
    Page<Keycap> getKeycapByFilter(KeycapFilter filter, Pageable pageable);


}


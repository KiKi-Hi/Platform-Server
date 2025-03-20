package com.jiyoung.kikihi.platform.application.port.in.product;

import com.jiyoung.kikihi.platform.domain.product.Housing;
import com.jiyoung.kikihi.platform.domain.product.KeyCap;
import com.jiyoung.kikihi.platform.domain.product.Switch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetPartsUseCase {

    /*
        사용자가 키보드에 대한 정보를 얻는 유즈케이스
        관심 목록이 제일 위에 가도록 설정한다.
        필터링은 가격, 브랜드 , 색상으로 가능함
        각각 조립품(하우징, 키캡, 스위치 등)은 각 사이트의 구매 링크를 모아서 사용자가 한번에 결제하도록 만들 수 있음
     */

    // 부품 선택 시 조합 가격 반영

    // 하우징 상세 조회
    Housing getHousing(Long id);

    // 하우징 최신순
    Page<Housing> getHousings(Pageable pageable);

    // 하우징 인기순
    Page<Housing> getHousingsByLike(Pageable pageable);

    // 하우징 조건별 , 조건문 추가해야 한다.
    Page<Housing> getHousingsByCondition(Pageable pageable);

    // 하우징 관심목록
    Page<Housing> getHousingsByWishList(Pageable pageable);


    ///  -----------------------------------------------

    // 키캡 상세 조회
    KeyCap getKeyCap(Long id);

    // 키캡 최신순
    Page<KeyCap> getKeyCaps(Pageable pageable);

    // 키캡 인기순
    Page<KeyCap> getKeyCapsByLike(Pageable pageable);

    // 키캡 조건별 , 조건문 추가해야 한다.
    Page<KeyCap> getKeyCapsByCondition(Pageable pageable);

    // 키캡 관심목록
    Page<KeyCap> getKeyCapsByWishList(Pageable pageable);

    ///  -----------------------------------------------

    // 스위치 상세 조회
    Switch getSwitch(Long id);

    // 스위치 최신순
    Page<Switch> getSwitches(Pageable pageable);

    // 스위치 인기순
    Page<Switch> getSwitchesByLike(Pageable pageable);

    // 스위치 조건별 , 조건문 추가해야 한다.
    Page<Switch> getSwitchesByCondition(Pageable pageable);

    // 스위치 관심목록
    Page<Switch> getSwitchesByWishList(Pageable pageable);// 스위치 조건별

    // 기타

}

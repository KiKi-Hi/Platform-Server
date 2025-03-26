package com.jiyoung.kikihi.platform.domain.product.frame;

import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Frame extends Product {

    private Layout layout; // 키보드 레이아웃 (예: 풀배열, 텐키리스 등)
    private Material materials; // 재질 (예: 알루미늄, 폴리카보네이트)
    private MountType mountType; // 장착 방식 (예: 트레이 마운트, 가스켓 마운트)
    private double weight; // 무게 (kg 단위 등으로 표현)
    private String soundDampening; // 흡음 처리 여부 (예: 실리콘 패드, 폼 추가 여부)


}

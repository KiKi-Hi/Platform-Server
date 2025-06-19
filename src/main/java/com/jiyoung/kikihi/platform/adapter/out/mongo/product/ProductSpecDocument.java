package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 제품 상세 스펙 정보를 담는 클래스입니다.
 * MongoDB의 Embedded Document 용도로 사용됩니다.
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpecDocument {

    @Field("제조회사")
    private String manufacturer;

    @Field("등록년월")
    private String releaseDate;

    @Field("사이즈")
    private String sizeType;

    @Field("연결 방식")
    private String connectionType;

    @Field("접점 방식")
    private String switchType;

    @Field("키 배열")
    private String keyLayout;

    @Field("인터페이스")
    private String interfaceType;

    @Field("기능 > 매크로 기능")
    private String macroSupport;

    @Field("키보드형태 > 비키스타일")
    private boolean vikiStyle;

    @Field("키보드구조 > RGB 백라이트")
    private boolean rgbBacklight;

    @Field("키보드구조 > 금속하우징")
    private boolean metalHousing;

    @Field("키보드구조 > 스위치 교체형")
    private boolean hotSwappable;

    @Field("키보드구조 > 흡음재")
    private boolean dampening;

    @Field("케이블 > 착탈식 케이블")
    private boolean detachableCable;

    @Field("크기(가로x세로x높이) > 가로")
    private float widthMm;

    @Field("크기(가로x세로x높이) > 세로")
    private float heightMm;

    @Field("크기(가로x세로x높이) > 높이")
    private float depthMm;

    @Field("크기(가로x세로x높이) > 무게")
    private int weightG;

    @Field("크기(가로x세로x높이) > 케이블 길이")
    private int cableLengthCm;

    @Field("제품 보증 > A/S 보증기간")
    private String warrantyPeriod;

    @Field("KC인증 > 적합성평가인증")
    private String kcCertConformity;

    @Field("KC인증 > 안전확인인증")
    private String kcCertSafety;
}

package com.jiyoung.kikihi.platform.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 제품 상세 스펙 정보를 담는 클래스입니다.
 * 도메인 용도로 사용됩니다.
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpec {

    private String manufacturer;

    private String releaseDate;

    private String sizeType;

    private String connectionType;

    private String switchType;

    private String keyLayout;

    private String interfaceType;

    private String macroSupport;

    private boolean vikiStyle;

    private boolean rgbBacklight;

    private boolean metalHousing;

    private boolean hotSwappable;

    private boolean dampening;

    private boolean detachableCable;

    private float widthMm;

    private float heightMm;

    private float depthMm;

    private int weightG;

    private int cableLengthCm;

    private String warrantyPeriod;

    private String kcCertConformity;

    private String kcCertSafety;
}

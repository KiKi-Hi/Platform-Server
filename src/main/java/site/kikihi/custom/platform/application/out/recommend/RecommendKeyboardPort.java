package site.kikihi.custom.platform.application.out.recommend;

import site.kikihi.custom.platform.adapter.in.web.dto.request.product.KeyboardOptions;
import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocument;

import java.util.List;
import java.util.UUID;

public interface RecommendKeyboardPort {

    // 키보드 추천 필터링 & 조회
    /**
     * 키보드 추천 필터링 및 조회
     *
     * @param userId         요청 사용자 식별자 (로그, 개인화 목적으로 활용 가능)
     * @param size           키보드 배열(Size) 옵션 (필수)
     * @param keyPressure    키압(Key Pressure) 옵션 (선택)
     * @param keycapProfile  키캡 배열(Keycap Profile) 옵션 (필수)
     * @param switchType     스위치 종류(Switch Type) 옵션 (필수)
     * @param soundDampener  흡음재 적용 여부 (선택)
     * @param rgb            RGB 적용 여부 (선택)
     * @param brand          제조사(Brand) 옵션 (선택)
     * @param keycapMaterial 키캡 재질 옵션 (선택)
     * @param minPrice       최소 가격 (필수)
     * @param maxPrice       최대 가격 (필수)
     * @return 필터링 된 추천 키보드 목록 (DTO 등 도메인 타입으로 변환 가능)
     */
    List<ProductDocument> filterAndRecommendKeyboards(
            UUID userId,
            KeyboardOptions.Size size,
            KeyboardOptions.KeyPressure keyPressure,
            KeyboardOptions.KeycapProfile keycapProfile,
            KeyboardOptions.SwitchType switchType,
            KeyboardOptions.SoundDampener soundDampener,
            KeyboardOptions.RGB rgb,
            String brand,
            KeyboardOptions.KeycapMaterial keycapMaterial,
            int minPrice,
            int maxPrice
    );
}

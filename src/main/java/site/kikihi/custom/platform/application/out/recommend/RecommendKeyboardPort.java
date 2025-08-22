package site.kikihi.custom.platform.application.out.recommend;

import site.kikihi.custom.platform.adapter.in.web.dto.request.product.KeyboardOptions;
import site.kikihi.custom.platform.domain.product.Product;

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
     * @param layout         키보드 배열의 크기 )
     * @param switchType     스위치 종류(Switch Type) 옵션 (필수)
     * @param soundDampener  흡음재 적용 여부 (선택)
     * @param rgb            RGB 적용 여부 (선택)
     * @param minPrice       최소 가격 (필수)
     * @param maxPrice       최대 가격 (필수)
     * @return 필터링 된 추천 키보드 목록 (DTO 등 도메인 타입으로 변환 가능)
     */
    List<Product> filterAndRecommendKeyboards(
            UUID userId,
            String size,
            Integer keyPressure,
            String layout,
            List<String> switchType,
            String soundDampener,
            String rgb,
            int minPrice,
            int maxPrice
    );

    // 유사한 상품 추천
    /**
     * 유사한 상품 추천
     *
     * @param userId      요청 사용자 식별자 (로그, 개인화 목적으로 활용 가능)
     * @param productId   추천할 상품의 식별자
     * @return 유사한 상품 목록 (DTO 등 도메인 타입으로 변환 가능)
     */
    List<Product> getSimilarProducts(UUID userId, String productId,Product baseProduct);
}

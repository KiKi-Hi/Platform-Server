package com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 어울리는 상품 응답 DTO
 *
 * @param id        상품 ID
 * @param name      상품명
 * @param thumbnail 썸네일 이미지 URL
 */

@Builder
@Schema(
        name = "[응답][상품] 어울리는 상품 응답 Response",
        description = "타 상품과 어울리는 추천 상품 조회 응답 DTO입니다."
)
record RecommendedItemResponse(
        @Schema(description = "상품 아이디", example = "202")
        String id,

        @Schema(description = "상품명", example = "키캡 악세사리")
        String name,

        @Schema(description = "상품 썸네일 이미지 URL", example = "https://example.com/recommended/202.jpg")
        String thumbnail
) {
}

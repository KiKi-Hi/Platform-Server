package com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product;

import lombok.Builder;

/**
 * 어울리는 상품 응답 DTO
 *
 * @param id        상품 ID
 * @param name      상품명
 * @param thumbnail 썸네일 이미지 URL
 */

@Builder
record RecommendedItemResponse(
        String id,
        String name,
        String thumbnail
) {
}

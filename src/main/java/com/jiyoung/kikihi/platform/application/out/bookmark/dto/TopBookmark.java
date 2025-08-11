package com.jiyoung.kikihi.platform.application.out.bookmark.dto;

import lombok.Builder;

/**
 * 인기 상품의 북마크 수를 나타내는 DTO.<br>
 * 클린 아키텍처 원칙에 따라 Application 계층에서 Adapter/JPA 타입에 직접 의존하지 않기 위해 정의함.
 * Adapter(JPA 등)에서 조회한 Projection을 본 DTO로 변환해 반환해야 한다.
 * @param productId 상품 ID
 * @param count     개수
 */
@Builder
public record TopBookmark(
        String productId,
        long count
) {

    /// 정적 팩토리 메서드
    public static TopBookmark of(String productId, long count) {
        return TopBookmark.builder()
                .productId(productId)
                .count(count)
                .build();
    }
}

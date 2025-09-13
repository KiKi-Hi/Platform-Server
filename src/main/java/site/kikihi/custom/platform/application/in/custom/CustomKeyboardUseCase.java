package site.kikihi.custom.platform.application.in.custom;

import org.springframework.data.domain.Pageable;
import site.kikihi.custom.platform.adapter.in.web.dto.request.custom.CustomKeyboardRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.domain.custom.CustomKeyboard;
import site.kikihi.custom.platform.domain.custom.CustomKeyboardLayout;
import site.kikihi.custom.platform.domain.custom.CustomKeyboardWithName;
import org.springframework.data.domain.Slice;

import java.util.*;

/**
 * 커스텀 키보드 관련 인터페이스입니다.
 * - 배열 조회
 * - 내 키보드 생성/수정/삭제
 * - 실제 상품 카테고리별 목록 조회
 * - 필터링(가격대)
 * - 좋아요
 * - 이름 저장하기
 * - 부품 추가하기
 * - 내 키보드 목록 조회
 * - 내 키보드 상세 조회
 */
public interface CustomKeyboardUseCase {

    /// 생성
    CustomKeyboard saveCustomKeyboard(CustomKeyboardRequest request, UUID userId);

    /// 조회

    // 나의 커스텀 키보드 목록 조회
    Slice<CustomKeyboardWithName> getCustomKeyboards(UUID userId);

    // 커스텀 키보드 상세 조회
    CustomKeyboardWithName getCustomKeyboard(Long customKeyboardId);

    // 배열에 맞는 상품 조회하기
    Slice<ProductListResponse> getCustomProducts(UUID userId, String categoryId, CustomKeyboardLayout type, Pageable pageable);

    // 배열에 맞는 상품 조회하기 (북마크있는 상품만 조회)
    Slice<ProductListResponse> getProductsByBookmark(UUID userId, String categoryId, CustomKeyboardLayout type, Pageable pageable);

    // 배열에 맞는 상품 개수 조회하기
    Long getCustomProductCounts(String categoryId, CustomKeyboardLayout type);

    // 배열에 맞는 상품 개수 조회하기(북마크)
    Long getCustomProductCountsByBookmark(UUID userId, String categoryId, CustomKeyboardLayout type);

    /// 수정
    // 커스텀 상품 내부에 부품 추가하기
    void insertProductInCustomKeyboard(Long customKeyboardId, String categoryId, String productId, UUID userId);

    /// 삭제
    void deleteCustomKeyboard(Long customKeyboardId, UUID userId);

    // 커스텀 상품 내부에서 부품 제거하기
    void deleteCustomInside(Long customKeyboardId, String categoryId, String productId, UUID userId);

}

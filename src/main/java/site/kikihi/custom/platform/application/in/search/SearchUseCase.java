package site.kikihi.custom.platform.application.in.search;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.domain.search.Search;

import java.util.List;
import java.util.UUID;

/**
 * 검색을 위한 유즈케이스입니다
 * - 키워드 기반 검색
 * - 나의 최근 검색어 조회
 * - 나의 최근 검색어 삭제
 * - 나의 최근 검색어 저장 끄기
 */
public interface SearchUseCase {

    /// 검색
    Slice<ProductListResponse> searchProducts(String keyword, int page, int size, UUID userId);

    /// 나의 검색에 목록 확인하기
    List<Search> getMySearches(UUID userId);

    /// 키워드 검색 결과 개수
    long countByKeyword(String keyword);

    /// 키워드 하나 삭제하기
    void deleteMySearchKeyword(Long searchId, UUID userId);

    /// 키워드 모두 삭제하기
    void deleteAllKeywords(UUID userId);
}

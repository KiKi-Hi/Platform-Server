package site.kikihi.custom.platform.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.elasticsearch.core.SearchHits;
import site.kikihi.custom.global.response.ErrorCode;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.adapter.out.elasticSearch.ProductESDocument;
import site.kikihi.custom.platform.application.in.search.SearchUseCase;
import site.kikihi.custom.platform.application.out.bookmark.BookmarkPort;
import site.kikihi.custom.platform.application.out.search.SearchPort;
import site.kikihi.custom.platform.application.out.user.UserPort;
import site.kikihi.custom.platform.domain.bookmark.Bookmark;
import site.kikihi.custom.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import site.kikihi.custom.platform.domain.search.Search;
import site.kikihi.custom.platform.domain.user.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService implements SearchUseCase {

    /// 의존성
    private final ElasticsearchOperations elasticsearchOperations;
    private final SearchPort port;

    /// 외부 의존성
    private final UserPort userPort;
    private final BookmarkPort bookmarkPort;

    /// 스태틱
    private final Float minScore = 0.001f;

    /// 키워드 검색 (name, description)
    @Override
    public Slice<ProductListResponse> searchProducts(String keyword, Pageable pageable, UUID userId) {

        /// Pageable 구성

        // match 쿼리 구성
        Query nameMatch = MatchQuery.of(m -> m.field("name").query(keyword))._toQuery();
        Query descMatch = MatchQuery.of(m -> m.field("description").query(keyword))._toQuery();

        // bool 쿼리
        Query boolQuery = BoolQuery.of(b -> b
                .should(nameMatch)
                .should(descMatch)
                .minimumShouldMatch("1")
        )._toQuery();

        // NativeQuery
        NativeQuery query = NativeQuery.builder()
                .withQuery(boolQuery)
                .withPageable(pageable)
                .withMinScore(minScore)
                .build();

        /// 로그인 한 유저가 확인한다면, 최근 검색 기록 DB에 저장하기
        if (userId != null) {

            /// 유저 조회
            User user = getUser(userId);

            /// DB에 최신 검색어 저장하기
            Search search = Search.of(user.getId(), keyword);
            port.saveSearch(search);

        }

        SearchHits<ProductESDocument> searchHits = elasticsearchOperations.search(query, ProductESDocument.class);

        /// 결과물 출력
        List<Product> elasticProducts = searchHits.stream()
                .map(SearchHit::getContent)
                .map(ProductESDocument::toDomain)
                .toList();

        log.info("Searching for {}", elasticProducts.toString());


        /// 페이징 처리
        // 현재 페이지 결과 수
        boolean hasNext = checkNext(pageable.getPageNumber(), pageable.getPageSize(), searchHits);

        Slice<Product> products = new SliceImpl<>(elasticProducts, pageable, hasNext);

        return toProductListResponse(userId, products);
    }

    private boolean checkNext(int page, int size, SearchHits<ProductESDocument> searchHits) {

        long totalHits = searchHits.getTotalHits();

        // 현재 페이지와 size를 이용해 현재 페이지가 마지막 페이지인지 판단
        boolean hasNext = (page + 1) * size < totalHits;

        return hasNext;
    }

    @Override
    public List<Search> getMySearches(UUID userId) {

        /// 유저
        User user = getUser(userId);

        /// 유저의 최근 검색어 조회하기
        return port.getSearches(user.getId());
    }

    /**
     * 특정 키워드를 검색 기록에서 삭제합니다.
     * @param searchId   삭제할 키워드
     * @param userId     유저 ID
     */
    @Override
    public void deleteMySearchKeyword(Long searchId, UUID userId) {

        /// 유저 예외 처리
        User user = getUser(userId);

        /// 검색 기록 예외 처리
        Search search = getSearch(searchId);

        /// 유저와 특정 키워드를 바탕으로 삭제합니다.
        try {
            port.deleteSearch(search.getId(), user.getId());
        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorCode.UNAUTHORIZED_DELETE_SEARCH.getMessage());
        }

    }

    /**
     * 모든 키워드를 검색 기록에서 삭제합니다.
     * @param userId    유저ID
     */
    @Override
    public void deleteAllKeywords(UUID userId) {

        /// 유저 예외 처리
        User user = getUser(userId);

        /// 전부 삭제하기
        port.deleteALlSearch(user.getId());
    }

    /**
     * 키워드에 따른 검색으로 검색 결과가 몇 개인지
     * @param keyword   키워드
     */
    @Override
    public long countByKeyword(String keyword) {
        // match 쿼리 구성
        Query nameMatch = MatchQuery.of(m -> m.field("name").query(keyword))._toQuery();
        Query descMatch = MatchQuery.of(m -> m.field("description").query(keyword))._toQuery();

        // bool 쿼리
        Query boolQuery = BoolQuery.of(b -> b
                .should(nameMatch)
                .should(descMatch)
                .minimumShouldMatch("1")
        )._toQuery();

        // NativeQuery - 페이징 없이 전체 개수 조회용
        NativeQuery query = NativeQuery.builder()
                .withQuery(boolQuery)
                .withMinScore(minScore)
                .build();

        SearchHits<ProductESDocument> searchHits = elasticsearchOperations.search(query, ProductESDocument.class);

        return searchHits.getTotalHits();
    }


    /// 유저 조회
    private User getUser(UUID userId) {

        return userPort.loadUserById(userId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.USER_NOT_FOUND.getMessage()));
    }


    /// 검색 기록 조회
    private Search getSearch(Long searchId) {

        return port.getSearch(searchId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.SEARCH_NOT_FOUND.getMessage()));
    }


    /**
     * 상품 목록 조회를 진행할때, 북마크 여부를 파악하는 함수입니다.
     * @param userId        유저 ID
     * @param products      상품 목록
     */
    private Slice<ProductListResponse> toProductListResponse(UUID userId, Slice<Product> products) {
        /// 응답 값
        List<ProductListResponse> dtoList;

        /// 상품 목록 꺼내서 DTO 변환
        List<Product> content = products.getContent();

        /// 로그인 하지 않은 유저가 확인한다면
        if (userId == null) {

            /// 하트가 전부 false 되는 로직
            dtoList = ProductListResponse.from(content);

            /// 새로운 Slice 객체로 생성
            return new SliceImpl<>(dtoList, products.getPageable(), products.hasNext());
        }

        /// 유저가 북마크를 했는지 체크
        List<Bookmark> bookmarks = bookmarkPort.getBookmarksByUserId(userId);

        /// 북마크된 상품 ID만 추출
        Set<String> bookmarkedProductIds = bookmarks.stream()
                .map(Bookmark::getProductId)
                .collect(Collectors.toSet());

        // 북마크 여부 반영하여 DTO 변환
        dtoList = ProductListResponse.from(content, bookmarkedProductIds);

        return new SliceImpl<>(dtoList, products.getPageable(), products.hasNext());
    }

}

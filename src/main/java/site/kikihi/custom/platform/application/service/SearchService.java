package site.kikihi.custom.platform.application.service;

import site.kikihi.custom.global.response.ErrorCode;
import site.kikihi.custom.platform.adapter.out.elasticSearch.ProductESDocument;
import site.kikihi.custom.platform.adapter.out.elasticSearch.ProductESRepository;
import site.kikihi.custom.platform.application.in.search.SearchUseCase;
import site.kikihi.custom.platform.application.out.search.SearchPort;
import site.kikihi.custom.platform.application.out.user.UserPort;
import site.kikihi.custom.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService implements SearchUseCase {

    /// 의존성
    private final ElasticsearchOperations elasticsearchOperations;
    private final ProductESRepository productESRepository;
    private final SearchPort port;

    /// 외부 의존성
    private final UserPort userPort;

    /// 스태틱
    private final Float minScore = 0.001f;

    /// 키워드 검색 (name, description)
    @Override
    public List<Product> searchProducts(String keyword, int page, int size, UUID userId) {
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
                .withPageable(PageRequest.of(page, size)) //page-> from으로 자동 변환(from=page * size)
                .withMinScore(minScore)  // <<-- 추가됨
                .build();

        /// 로그인 한 유저가 확인한다면, 최근 검색 기록 DB에 저장하기
        if (userId != null) {

            /// 유저 조회
            User user = getUser(userId);

            /// DB에 최신 검색어 저장하기
            Search search = Search.of(user.getId(), keyword);
            port.saveSearch(search);
        }

        return elasticsearchOperations.search(query, ProductESDocument.class)
                .stream()
                .map(SearchHit::getContent)
                .map(ProductESDocument::toDomain)
                .collect(Collectors.toList());
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
     * 검색 기록을 저장하지않도록 끕니다.
     * @param userId    유저 ID
     */
    @Override
    public void turnOffMySearchKeyword(UUID userId) {

        /// 유저
        User user = getUser(userId);

        /// 이미 켜져있다면
        if (!user.isSearch()) {
            throw new IllegalStateException(ErrorCode.ALREADY_ON.getMessage());
        }

        /// 비즈니스 로직 수행
        user.turnOffSearch();

        /// 업데이트
        userPort.updateUser(user);
    }

    @Override
    public void turnOnMySearchKeyword(UUID userId) {
        /// 유저
        User user = getUser(userId);

        /// 이미 켜져있다면
        if (user.isSearch()) {
            throw new IllegalStateException(ErrorCode.ALREADY_ON.getMessage());
        }

        /// 비즈니스 로직 수행
        user.turnOnSearch();

        /// 업데이트
        userPort.updateUser(user);

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

}

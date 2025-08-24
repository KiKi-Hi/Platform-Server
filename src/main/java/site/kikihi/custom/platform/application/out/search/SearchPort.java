package site.kikihi.custom.platform.application.out.search;

import site.kikihi.custom.platform.domain.search.Search;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SearchPort {

    /// 최근 검색어 저장
    Search saveSearch(Search search);

    Optional<Search> getSearch(Long id);

    /// 유저의 최근 검색어 보기
    List<Search> getSearches(UUID userId);

    /// 최근 검색어 하나 삭제하기
    void deleteSearch(Long searchId, UUID userId);

    /// 최근 검색어 모두 삭제하기
    void deleteALlSearch(UUID userId);

}

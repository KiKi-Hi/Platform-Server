package site.kikihi.custom.platform.adapter.in.web.dto.response.search;

import lombok.Builder;
import site.kikihi.custom.platform.domain.search.Search;
import java.util.List;

@Builder
public record SearchListResponse(
        Long searchId,
        String keyword
) {

    /// 정적 팩토리 메서드
    public static SearchListResponse from(Search search) {
        return SearchListResponse.builder()
                .searchId(search.getId())
                .keyword(search.getKeyword())
                .build();
    }

    /// 정적 팩토리 메서드
    public static List<SearchListResponse> from(List<Search> searches) {
        return searches.stream()
                .map(SearchListResponse::from)
                .toList();
    }
}

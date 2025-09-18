package site.kikihi.custom.platform.adapter.in.web.dto.response.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import site.kikihi.custom.platform.domain.search.Search;
import java.util.List;

@Builder
@Schema(
        name = "[응답][검색] 최신 검색어 조회 Response",
        description = "사용자의 검색어 정보를 반환하는 응답 DTO입니다."
)
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

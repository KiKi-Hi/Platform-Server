package site.kikihi.custom.platform.domain.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.kikihi.custom.platform.domain.BaseDomain;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Search extends BaseDomain {

    private Long id;

    private UUID userId;

    private String keyword;


    /// 정적 팩토리 메서드
    public static Search of(UUID userId, String keyword) {
        return Search.builder()
                .userId(userId)
                .keyword(keyword)
                .build();
    }

}

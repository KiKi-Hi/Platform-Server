package site.kikihi.custom.platform.adapter.out.jpa.search;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.kikihi.custom.platform.adapter.out.jpa.BaseTimeEntity;
import site.kikihi.custom.platform.domain.search.Search;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SearchJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId;

    private String keyword;

    /// 정적 팩토리 메서드
    public static SearchJpaEntity from(Search search) {
        return SearchJpaEntity.builder()
                .userId(search.getUserId())
                .keyword(search.getKeyword())
                .build();
    }

    /// 도메인
    public Search toDomain() {
        return Search.builder()
                .id(id)
                .keyword(keyword)
                .userId(userId)
                .build();

    }
}

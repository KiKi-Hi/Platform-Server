package site.kikihi.custom.platform.adapter.out.jpa.search;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SearchJpaRepository extends JpaRepository<SearchJpaEntity, Long> {

    List<SearchJpaEntity> findByUserIdOrderByCreatedAtDesc(UUID userId);

    void deleteAllByUserId(UUID userId);

    void deleteByIdAndUserId(Long id, UUID userId);
}

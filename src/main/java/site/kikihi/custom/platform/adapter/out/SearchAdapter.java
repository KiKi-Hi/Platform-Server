package site.kikihi.custom.platform.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.kikihi.custom.platform.adapter.out.jpa.search.SearchJpaEntity;
import site.kikihi.custom.platform.adapter.out.jpa.search.SearchJpaRepository;
import site.kikihi.custom.platform.application.out.search.SearchPort;
import site.kikihi.custom.platform.domain.search.Search;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SearchAdapter implements SearchPort {

    private final SearchJpaRepository repository;

    @Override
    public Search saveSearch(Search search) {

        var entity = SearchJpaEntity.from(search);
        return repository.save(entity)
                .toDomain();
    }

    @Override
    public Optional<Search> getSearch(Long id) {
        return repository.findById(id)
                .map(SearchJpaEntity::toDomain);
    }

    @Override
    public List<Search> getSearches(UUID userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(SearchJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteSearch(Long searchId, UUID userId) {
        repository.deleteByIdAndUserId(searchId, userId);
    }


    @Override
    public void deleteALlSearch(UUID userId) {
        repository.deleteAllByUserId(userId);
    }
}

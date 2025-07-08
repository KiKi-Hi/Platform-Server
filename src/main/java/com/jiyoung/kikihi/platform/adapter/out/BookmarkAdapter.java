package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.bookmark.BookmarkJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.bookmark.BookmarkJpaRepository;
import com.jiyoung.kikihi.platform.application.out.bookmark.BookmarkPort;
import com.jiyoung.kikihi.platform.domain.bookmark.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class BookmarkAdapter implements BookmarkPort {

    private final BookmarkJpaRepository repository;


    @Override
    public Bookmark saveBookmark(Bookmark bookmark) {

        var entity = BookmarkJpaEntity.from(bookmark);

        return repository.save(entity)
                .toDomain();
    }

    @Override
    public Optional<Bookmark> getBookmark(Long id) {
        return repository.findById(id)
                .map(BookmarkJpaEntity::toDomain);
    }

    @Override
    public List<Bookmark> getBookmarksByUserIdAndCategoryId(UUID userId, String category) {
        return repository.findByUserIdAndCategory(userId, category).stream()
                .map(BookmarkJpaEntity::toDomain)
                .toList();
    }

    @Override
    public boolean checkBookmarkByIdAndUserId(Long bookmarkId, UUID userId) {
        return repository.existsByUserIdAndId(userId, bookmarkId);
    }

    @Override
    public void deleteBookmarkById(Long bookmarkId) {
        repository.deleteById(bookmarkId);
    }
}

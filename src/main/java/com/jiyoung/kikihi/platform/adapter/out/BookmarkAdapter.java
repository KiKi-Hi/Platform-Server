package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.bookmark.BookmarkJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.bookmark.BookmarkJpaRepository;
import com.jiyoung.kikihi.platform.application.out.bookmark.BookmarkPort;
import com.jiyoung.kikihi.platform.domain.bookmark.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public boolean checkBookmarkByUserIdAndProductId(UUID userId, String productId) {
        return repository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    public boolean checkBookmarkByProductIdAndUserId(String productId, UUID userId) {
        return repository.existsByProductIdAndUserId(productId, userId);
    }

    @Override
    public Map<String, Long> getFavoriteBookmarks() {

        ///  북마크 많은 순서로 DB 조회
        List<Object[]> result = repository.findProductIdAndBookmarkCountOrderByCountDesc();

        /// 맵 생성
        Map<String, Long> map = new HashMap<>();

        /// 맵 데이터 넣기
        for (Object[] o : result) {
            map.put(String.valueOf(o[0]), (Long) o[1]);
        }
        return map;
    }

    @Override
    public void deleteBookmarkById(Long bookmarkId) {
        repository.deleteById(bookmarkId);
    }
}

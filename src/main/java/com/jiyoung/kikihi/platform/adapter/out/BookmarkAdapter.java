package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.bookmark.BookmarkJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.bookmark.BookmarkJpaRepository;
import com.jiyoung.kikihi.platform.adapter.out.jpa.bookmark.projection.ProductIdWithCount;
import com.jiyoung.kikihi.platform.application.out.bookmark.BookmarkPort;
import com.jiyoung.kikihi.platform.application.out.bookmark.dto.TopBookmark;
import com.jiyoung.kikihi.platform.domain.bookmark.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 북마크 DB와 관련된 아답터 입니다.
 */
@Component
@Transactional
@RequiredArgsConstructor
public class BookmarkAdapter implements BookmarkPort {

    /// JPA 레포지토리 주입
    private final BookmarkJpaRepository repository;

    // =================
    //  저장 함수
    // =================

    /**
     * 저장 기능
     * @param bookmark  저장할 북마크 도메인
     */
    @Override
    public Bookmark saveBookmark(Bookmark bookmark) {

        var entity = BookmarkJpaEntity.from(bookmark);

        return repository.save(entity)
                .toDomain();
    }

    // =================
    //  조회 함수
    // =================

    /**
     * 아이디 기반 상세 조회
     * @param id    북마크 아이디
     */
    @Override
    public Optional<Bookmark> getBookmark(Long id) {
        return repository.findById(id)
                .map(BookmarkJpaEntity::toDomain);
    }

    /**
     * 유저가 북마크한 카테고리별 상품 조회
     * @param userId        유저 ID
     * @param category      카테고리
     */
    @Override
    public List<Bookmark> getBookmarksByUserIdAndCategoryId(UUID userId, String category) {
        return repository.findByUserIdAndCategory(userId, category).stream()
                .map(BookmarkJpaEntity::toDomain)
                .toList();
    }

    /**
     * 북마크ID 와 유저가 존재하는지 체크
     * @param bookmarkId    북마크 ID
     * @param userId        유저 ID
     */
    @Override
    public boolean checkBookmarkByIdAndUserId(Long bookmarkId, UUID userId) {
        return repository.existsByUserIdAndId(userId, bookmarkId);
    }

    /**
     * 특정 상품에 유저가 북마크한 적이 있는지 체크
     * @param userId        유저 ID
     * @param productId     상품 ID
     */
    @Override
    public boolean checkBookmarkByUserIdAndProductId(UUID userId, String productId) {
        return repository.existsByUserIdAndProductId(userId, productId);
    }

    /**
     * limit 만큼 인기있는 상품 목록 조회
     * @param limit 조회할 개수
     */
    @Override
    public List<TopBookmark> listTopBookmarks(int limit) {

        /// DB에 조회
        List<ProductIdWithCount> bookmarkAndCount = repository.findBookmarkAndCount(Pageable.ofSize(limit));

        /// 서비스로직에서 DTO로 수정
        return bookmarkAndCount.stream()
                .map(bc -> TopBookmark.of(bc.getProductId(), bc.getCount()))
                .toList();
    }

    /**
     * 북마크가 몇 개 되어있는지 체크
     */
    @Override
    public Long countBookmarks() {
        return repository.countByProductId();
    }

    // =================
    //  삭제 함수
    // =================

    /**
     * 북마크 삭제하기
     * @param bookmarkId    삭제할 북마크 ID
     */
    @Override
    public void deleteBookmarkById(Long bookmarkId) {
        repository.deleteById(bookmarkId);
    }

    /**
     * 북마크 전체 삭제하기
     */
    @Override
    public void deleteAllBookmarks() {
        repository.deleteAll();
    }
}

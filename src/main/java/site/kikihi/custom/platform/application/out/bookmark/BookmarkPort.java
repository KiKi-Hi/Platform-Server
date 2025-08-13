package site.kikihi.custom.platform.application.out.bookmark;

import site.kikihi.custom.platform.application.out.bookmark.dto.TopBookmark;
import site.kikihi.custom.platform.domain.bookmark.Bookmark;

import java.util.*;

/**
 * [DB에 북마크를 저장하는 Port 인터페이스] 입니다.
 * - DB 저장
 * - DB 조회
 * - DB 삭제
 */

public interface BookmarkPort {

    // =================
    //  북마크 저장
    // =================
    /// 저장하기
    Bookmark saveBookmark(Bookmark bookmark);

    // =================
    //  북마크 조회
    // =================
    /// 북마크 상세 조회하기
    Optional<Bookmark> getBookmark(Long id);

    /// 유저와 카테고리 ID를 바탕으로 북마크 목록 조회
    List<Bookmark> getBookmarksByUserIdAndCategoryId(UUID userId, String category);

    /// 유저와 북마크 ID를 바탕으로 북마크 여부 조회
    boolean checkBookmarkByUserIdAndId(UUID userId, Long bookmarkId);

    /// 유저와 상품을 바탕으로 북마크 여부 조회
    boolean checkBookmarkByUserIdAndProductId(UUID userId, String productId);

    /// 북마크 상위 N개 인기순
    List<TopBookmark> listTopBookmarks(int limit);

    /// 북마크에 존재하는 상품 개수 세기
    Long countBookmarks();

    // =================
    //  북마크 삭제
    // =================
    /// 삭제하기
    void deleteBookmarkById(Long bookmarkId);

    /// 북마크 전부 삭제하기
    void deleteAllBookmarks();
}

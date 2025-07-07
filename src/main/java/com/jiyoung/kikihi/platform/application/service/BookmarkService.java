package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.BookmarkRequest;
import com.jiyoung.kikihi.platform.application.in.bookmark.BookmarkUseCase;
import com.jiyoung.kikihi.platform.application.out.bookmark.BookmarkPort;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.application.out.user.UserPort;
import com.jiyoung.kikihi.platform.domain.bookmark.Bookmark;
import com.jiyoung.kikihi.platform.domain.product.Product;
import com.jiyoung.kikihi.platform.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService implements BookmarkUseCase {

    private final BookmarkPort port;

    /// 외부 의존성
    private final UserPort userPort;
    private final ProductPort productPort;

    /**
     * 북마크 생성
     * @param request Bookmark 생성을 위한 Request
     */
    @Override
    public Bookmark saveBookmark(BookmarkRequest request) {

        /// 유저 예외 처리
        User user = getUser(request.getUserId());

        /// 상품 예외 처리
        Product product = productPort.getProduct(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));

        /// 상품을 바탕으로 카테고리 저장
        Bookmark bookmark = Bookmark.of(product.getId(), user.getId(), product.getCategory());

        return port.saveBookmark(bookmark);
    }


    /**
     * 북마크 상세 조회
     * @param id 조회할 북마크 Id
     */
    @Override
    public Bookmark loadBookmarkById(Long id) {
        /// 북마크 조회
        return port.getBookmark(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.BOOKMARK_NOT_FOUND.getMessage()));

    }

    /**
     * 카테고리 기반 북마크 조회
     * @param userId    북마크 조회할 유저 Id
     * @param category  조회할 상품 카테고리
     */
    @Override
    public List<Bookmark> loadBookmarksByUserIdAndCategory(UUID userId, String category) {

        /// 유저 검증
        User user = getUser(userId);

        /// 해당 카테고리가 존재하는지 체크

        // TODO: 추후 카테고리를 Enum으로 변경후 진행할 예정입니다.

        /// 유저를 바탕으로 해당 카테고리를 가진 아이템 목록 조회
        return port.getBookmarksByUserIdAndCategoryId(userId, category);
    }

    /**
     * 북마크 삭제 기능
     * @param id        삭제할 북마크 Id
     * @param userId    북마크를 삭제할 유저 Id
     */
    @Override
    public void deleteBookmarkById(Long id, UUID userId) {

        /// 유저 검증
        User user = getUser(userId);

        /// 해당 유저가 저장한 북마크인지 체크
        boolean checked = port.checkBookmarkByIdAndUserId(id, userId);

        if (!checked) {
            throw new IllegalStateException(ErrorCode.BOOKMARK_NOT_OWN_USER.getMessage());
        }

        /// 삭제
        port.deleteBookmarkById(id);

    }

    /// 공통 함수
    /**
     * ID를 바탕으로 DB에서 유저를 조회하는
     * @param userId 유저 ID
     */
    private User getUser(UUID userId) {
        return userPort.loadUserById(userId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

}

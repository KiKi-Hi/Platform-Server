package site.kikihi.custom.platform.application.service;

import site.kikihi.custom.global.response.ErrorCode;
import site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark.BookmarkRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark.BookmarkSyncRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.bookmark.BookmarkListResponse;
import site.kikihi.custom.platform.application.in.bookmark.BookmarkUseCase;
import site.kikihi.custom.platform.application.out.bookmark.BookmarkPort;
import site.kikihi.custom.platform.application.out.product.ProductPort;
import site.kikihi.custom.platform.application.out.user.UserPort;
import site.kikihi.custom.platform.domain.bookmark.Bookmark;
import site.kikihi.custom.platform.domain.product.Product;
import site.kikihi.custom.platform.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    public Bookmark saveBookmark(UUID userId, BookmarkRequest request) {

        /// 유저 예외 처리
        User user = getUser(userId);

        /// 상품 예외 처리
        Product product = getProduct(request.getProductId());

        /// 이미 눌렀다면 예외 처리 발생
        boolean checked = port.checkBookmarkByUserIdAndProductId(userId, request.getProductId());

        if (checked) {
            throw new IllegalStateException(ErrorCode.BOOKMARK_ALREADY.getMessage());
        }

        /// 상품을 바탕으로 카테고리 저장
        Bookmark bookmark = Bookmark.of(product.getId(), user.getId(), product.getCategory());

        return port.saveBookmark(bookmark);
    }



    /**
     * 로컬 스토리지에 저장한 북마크를 한번에 저장할 수 있도록 하는 서비스 로직
     *
     * @param userId  북마크를 저장할 유저 Id
     * @param request 저장할 요청 DTO
     */
    @Override
    public void syncBookmarks(UUID userId, BookmarkSyncRequest request) {

        /// 상품 ID들
        List<BookmarkRequest> productIds = request.getProductIds();

        /// 서비스를 연속으로 실현하여 작동
        List<Bookmark> list = productIds.stream()
                .map(req -> saveBookmark(userId, req))
                .toList();

    }

    /**
     * 나의 북마크 조회
     * @param userId    북마크 조회할 유저 Id
     * @param category  조회할 상품 카테고리
     */
    @Override
    public Slice<BookmarkListResponse> loadBookmarksByUserIdAndCategory(UUID userId, String category, Pageable pageable) {

        // 유저 검증
        User user = getUser(userId);

        // 해당 카테고리가 존재하는지 체크

        // TODO: 추후 카테고리를 Enum으로 변경후 진행할 예정입니다.

        // 유저를 바탕으로 해당 카테고리를 가진 아이템 목록 조회
        List<Bookmark> bookmarks = port.getBookmarksByUserIdAndCategoryId(user.getId(), category);

        // 상품 Id 목록 가져오기
        List<String> productIds = getProductIds(bookmarks);

        if (productIds.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList(), pageable, false);
        }

        // 상품 목록 정보 가져오기
        Slice<Product> products = productPort.getProductsByIds(productIds, pageable);

        // 매핑하기
        Map<String, Bookmark> bookmarkMap = bookmarks.stream()
                .collect(Collectors.toMap(Bookmark::getProductId, b -> b));

        List<BookmarkListResponse> content = products.getContent().stream()
                .map(product -> {
                    Bookmark bookmark = bookmarkMap.get(product.getId());
                    return BookmarkListResponse.from(bookmark, product);
                })
                .toList();

        return new SliceImpl<>(content, pageable, products.hasNext());
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
        boolean checked = port.checkBookmarkByUserIdAndId(user.getId(), id);

        if (!checked) {
            throw new IllegalStateException(ErrorCode.UNAUTHORIZED_DELETE_BOOKMARK.getMessage());
        }

        /// 삭제
        port.deleteBookmarkById(id);

    }

    /**
     * 북마크 삭제 기능 구현
     * @param ids       삭제할 북마크 Ids
     * @param userId    북마크를 삭제할 유저 Id
     */
    @Override
    public void deleteBookmarkById(List<Long> ids, UUID userId) {

        /// 삭제에 대한 반복문 실행

        /// TODO! 지울 때마다 유저가 생성한 북마크인지 체크하기 위해서 진행
        for (Long id : ids) {

            /// 서비스 로직 실행
            deleteBookmarkById(id, userId);
        }

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

    /**
     * 상품 Id 추출
     * @param bookmarks 북마크 목록
     * @return List<String>
     */
    private static List<String> getProductIds(List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(Bookmark::getProductId)
                .toList();
    }


    /**
     * 상품 내부 조회 함수
     * @param productId 상품 ID
     */
    private Product getProduct(String productId) {
        return productPort.getProduct(productId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
    }

}

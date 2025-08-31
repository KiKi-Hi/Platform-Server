package site.kikihi.custom.platform.application.service;

import site.kikihi.custom.global.response.ErrorCode;
import site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark.BookmarkRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.bookmark.BookmarkListResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocument;
import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocumentRepository;
import site.kikihi.custom.platform.application.out.bookmark.BookmarkPort;
import site.kikihi.custom.platform.application.out.user.UserPort;
import site.kikihi.custom.platform.domain.bookmark.Bookmark;
import site.kikihi.custom.platform.domain.product.Product;
import site.kikihi.custom.platform.domain.product.ProductFixtures;
import site.kikihi.custom.platform.domain.user.User;
import site.kikihi.custom.platform.domain.user.UserFixtures;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * [북마크 서비스에 대한 통합 테스트] 입니다.
 * - 유저 및 상품 정보는 테스트 시작 전 DB에 미리 저장됩니다.
 * - Happy/Unhappy 케이스를 모두 검증합니다.
 */

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookmarkServiceIntTest {

    @Autowired
    private BookmarkService sut;

    @Autowired
    private BookmarkPort port;

    @Autowired
    private UserPort userPort;

    @Autowired
    private ProductDocumentRepository productRepository;

    private final UUID id = UUID.fromString("12345678-aaaa-bbbb-cccc-123456789abc");
    private final UUID id2 = UUID.fromString("02345679-aaaa-bbbb-cccc-123456789abc");

    private User user;
    private User user2;
    private Product product1;
    private Product product2;
    private Product product3;

    /**
     * - 미리 유저와 상품 정보는 DB에 넣어두도록 세팅을 진행합니다.
     */
    @BeforeEach
    void setUp() {

        /// 유저 정보 저장
        user = userPort.saveUser(UserFixtures.createUser(id));
        user2 = userPort.saveUser(UserFixtures.createUser(id2));

        /// 상품 정보 저장
        product1 = productRepository.save(ProductFixtures.createProduct("test", "테스트 상품1",100000)).toDomain();
        product2 = productRepository.save(ProductFixtures.createProduct("test", "테스트 상품2",100000)).toDomain();
        product3 = productRepository.save(ProductFixtures.createProduct("test", "테스트 상품3",100000)).toDomain();
    }

    @Nested
    @DisplayName("생성 테스트")
    class BookmarkSave {

        @Test
        @DisplayName("[happy] 유저가 북마크 정상 생성")
        void saveBookmark_정상() {

            // given
            var request = BookmarkRequest.builder()
                    .productId(product1.getId())
                    .build();

            // when
            Bookmark bookmark = sut.saveBookmark(user.getId(), request);

            // then
            Optional<Bookmark> result = port.getBookmark(bookmark.getId());
            Assertions.assertThat(result.isPresent()).isTrue();
            Assertions.assertThat(result.get().getId()).isEqualTo(bookmark.getId());
            Assertions.assertThat(result.get().getUserId()).isEqualTo(bookmark.getUserId());
            Assertions.assertThat(result.get().getProductId()).isEqualTo(bookmark.getProductId());
        }

        @Test
        @DisplayName("[unhappy] 유저가 DB에 없는 경우 예외 발생")
        void saveBookmark_throw_user_NoSuchException() {

            // given
            User fakeUser = UserFixtures.fakeUser();

            var request = BookmarkRequest.builder()
                    .productId(product1.getId())
                    .build();

            // when & then
            Assertions.assertThatThrownBy(() -> sut.saveBookmark(fakeUser.getId(), request))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("[unhappy] 북마크하려는 상품이 없는 경우 예외 발생")
        void saveBookmark_throw_product_NoSuchException() {

            // given
            ProductDocument fakeProduct = ProductFixtures.fakeProduct();

            var request = BookmarkRequest.builder()
                    .productId(fakeProduct.getId())
                    .build();

            // when & then
            Assertions.assertThatThrownBy(() -> sut.saveBookmark(user.getId(),request))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("[unhappy] 이미 북마크한 경우, 추가적으로 북마크하려고 할 때 예외 발생")
        void saveBookmark_throw_product_IllegalStateException() {

            // given
            var request = BookmarkRequest.builder()
                    .productId(product1.getId())
                    .build();

            sut.saveBookmark(user.getId(),request);

            // when & then
            Assertions.assertThatThrownBy(() -> sut.saveBookmark(user.getId(),request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining(ErrorCode.BOOKMARK_ALREADY.getMessage());
        }
    }

    @Nested
    @DisplayName("조회 테스트")
    class BookmarkLoad {

        @Test
        @DisplayName("[happy] 유저의 카테고리별 북마크 정상 조회")
        void loadBookmark_category_정상() {

            // given
            Pageable pageable = PageRequest.of(0, 10);
            List<Bookmark> savedBookmarks = new ArrayList<>();

            List.of(product1, product2, product3).forEach(product -> {
                var request = BookmarkRequest.builder()
                        .productId(product.getId())
                        .build();
                savedBookmarks.add(sut.saveBookmark(user.getId(),request));
            });

            // when
            Slice<BookmarkListResponse> bookmarks = sut.loadBookmarksByUserIdAndCategory(user.getId(), "test", pageable);

            // then
            Assertions.assertThat(bookmarks).hasSize(3);
            Assertions.assertThat(bookmarks)
                    .extracting(BookmarkListResponse::products)
                    .extracting(ProductListResponse::id)
                    .containsExactlyInAnyOrder(product1.getId(), product2.getId(), product3.getId());

        }

        @Test
        @DisplayName("[happy] 북마크에 좋아요 여부 true 표시")
        void loadBookmark_좋아요_정상_조회() {

            // given
            Pageable pageable = PageRequest.of(0, 10);
            List<Bookmark> savedBookmarks = new ArrayList<>();

            /// 3개의 상품에 북마크 저장
            List.of(product1, product2, product3).forEach(product -> {
                var request = BookmarkRequest.builder()
                        .productId(product.getId())
                        .build();
                savedBookmarks.add(sut.saveBookmark(user.getId(),request));
            });

            // when
            Slice<BookmarkListResponse> bookmarks = sut.loadBookmarksByUserIdAndCategory(user.getId(), "test", pageable);

            // then
            Assertions.assertThat(bookmarks).hasSize(3);
            Assertions.assertThat(bookmarks)
                    .extracting(BookmarkListResponse::products)
                    .extracting(ProductListResponse::id)
                    .containsExactlyInAnyOrder(product1.getId(), product2.getId(), product3.getId());
            Assertions.assertThat(bookmarks)
                    .extracting(BookmarkListResponse::products)
                    .extracting(ProductListResponse::likedByMe)
                    .containsExactlyInAnyOrder(true, true, true);

        }
    }

    @Nested
    @DisplayName("삭제 테스트")
    class BookmarkDelete {

        @Test
        @DisplayName("[happy] 북마크 삭제 처리")
        public void deleteBookmark(){

            //given
            var request = BookmarkRequest.builder()
                    .productId(product1.getId())
                    .build();

            Bookmark saveBookmark = sut.saveBookmark(user.getId(),request);

            //when
            sut.deleteBookmarkById(saveBookmark.getId(), user.getId());

            // then
            Optional<Bookmark> bookmark = port.getBookmark(saveBookmark.getId());
            Assertions.assertThat(bookmark.isPresent()).isFalse();

        }


        @Test
        @DisplayName("[unhappy] 북마크 삭제 예외 처리")
        public void deleteBookmark_throw_otherUser_IllegalStateException(){

            //given
            var request = BookmarkRequest.builder()
                    .productId(product1.getId())
                    .build();
            Bookmark saveBookmark = sut.saveBookmark(user.getId(), request);

            //when & then
            Assertions.assertThatThrownBy(() -> sut.deleteBookmarkById(saveBookmark.getId(), user2.getId()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining(ErrorCode.UNAUTHORIZED_DELETE_BOOKMARK.getMessage());
        }

    }
}

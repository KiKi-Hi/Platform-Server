package site.kikihi.custom.platform.application.service;

import site.kikihi.custom.global.response.ErrorCode;
import site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark.BookmarkRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductDetailResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocument;
import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocumentRepository;
import site.kikihi.custom.platform.application.out.product.ProductPort;
import site.kikihi.custom.platform.application.out.user.UserPort;
import site.kikihi.custom.platform.domain.product.ProductFixtures;
import site.kikihi.custom.platform.domain.user.User;
import site.kikihi.custom.platform.domain.user.UserFixtures;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * [ 상품 조회 관련 통합 테스트 ]
 * 북마크를 진행했을 때, 여부가 포함되는지까지 테스트 합니다
 */
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ProductServiceIntTest {

    @Autowired
    private ProductService sut;

    @Autowired
    private ProductPort port;

    @Autowired
    private BookmarkService bookmarkService;


    /// 세팅을 위한 의존성
    @Autowired
    private ProductDocumentRepository repository;

    @Autowired
    private UserPort userPort;

    private ProductDocument product1;
    private ProductDocument product2;
    private ProductDocument product3;
    private ProductDocument product4;

    private final UUID id1 = UUID.fromString("12345678-aaaa-bbbb-cccc-123456789abc");
    private final UUID id2 = UUID.fromString("01234567-aaaa-bbbb-cccc-123456789abc");
    private final UUID id3 = UUID.fromString("90123456-aaaa-bbbb-cccc-123456789abc");

    private User user1;
    private User user2;
    private User user3;

    /// 테스트 세팅
    @BeforeEach()
    void setUp() {
        /// 테스트용 상품 저장하기
        product1 = ProductFixtures.createProduct("test", "test1",100000);
        product2 = ProductFixtures.createProduct("test", "test2",200000);
        product3 = ProductFixtures.createProduct("test", "test3",300000);
        product4 = ProductFixtures.createProduct("test", "test4","testManufacturer",400000);

        /// 유저 저장
        user1 = userPort.saveUser(UserFixtures.createUser(id1, "유저1", "socialId1"));
        user2 = userPort.saveUser(UserFixtures.createUser(id2, "유저2", "socialId2"));
        user3 = userPort.saveUser(UserFixtures.createUser(id3, "유저3", "socialId3"));

        /// DB에 모두 저장하기
        List<ProductDocument> documents = List.of(product1, product2, product3, product4);
        repository.saveAll(documents);
    }
    @AfterEach
    void tearDown() {

        repository.deleteAll();

    }

    /**
     * 상세 조회 서비스
     */
    @Nested
    @DisplayName("상세 조회 서비스")
    class detailLoadTest {

        @Test
        @DisplayName("[happy] 로그인 유저의 좋아요한 상품 상세 조회 테스트")
        void detailLoad_user_no_bookmark_happy() {

            // given
            var request = getBookmarkRequest(user1.getId(), product1.getId());

            bookmarkService.saveBookmark(request);

            // when
            ProductDetailResponse response = sut.getProduct(user1.getId(), product1.getId());

            // then
            assertNotNull(response);
            assertTrue(response.likedByMe());
            Assertions.assertEquals(response.productName(), product1.getName());
        }

        @Test
        @DisplayName("[happy] 로그인 유저의 좋아요 하지않은 상품 상세 조회 테스트")
        void detailLoad_user_bookmark_happy() {

            // given
            // 북마크 추가 X

            // when
            ProductDetailResponse response = sut.getProduct(user1.getId(), product2.getId());

            // then
            assertNotNull(response);
            Assertions.assertEquals(response.productName(), product2.getName());
            assertFalse(response.likedByMe());
        }

        @Test
        @DisplayName("[happy] 로그인 하지않은 유저의 상품 상세 조회 테스트")
        void detailLoad_happy() {

            // given

            // when
            ProductDetailResponse response = sut.getProduct(null, product3.getId());

            // then
            assertNotNull(response);
            Assertions.assertEquals(response.productName(), product3.getName());
            assertFalse(response.likedByMe());
        }

        @Test
        @DisplayName("[unhappy] 상품 상세 조회 예외처리 테스트")
        void detailLoad_throw_NoSuchElementException() {

            // given
            String productId = UUID.randomUUID().toString();

            // when & then
            org.assertj.core.api.Assertions.assertThatThrownBy(() -> sut.getProduct(null, productId))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }

    }

    /**
     * 카테고리 기반 테스트
     */
    @Nested
    @DisplayName("카테고리 기반 목록 조회 서비스")
    class listLoadTest_category {

        @Test
        @DisplayName("[happy] 로그인한 유저 북마크 한, 카테고리 기반 상품 목록 조회 테스트")
        void listLoad_category_user_bookmark_happy() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            // 상품1,3에 북마크 설정
            var request1 = getBookmarkRequest(user1.getId(), product1.getId());
            var request2 = getBookmarkRequest(user1.getId(), product3.getId());

            List.of(request1, request2)
                    .forEach(req -> bookmarkService.saveBookmark(req));

            // when
            Slice<ProductListResponse> products = sut.getProductsByCategoryId(user1.getId(), "test", pageable);

            // then
            List<ProductListResponse> content = products.getContent();
            assertEquals(4, content.size());

            content.forEach(product -> {
                if (product.id().equals(product1.getId()) || product.id().equals(product3.getId())) {
                    Assertions.assertTrue(product.likedByMe());
                }
                else {
                    Assertions.assertFalse(product.likedByMe());
                }
            });

        }

        @Test
        @DisplayName("[happy] 로그인한 유저 북마크 하지않은, 카테고리 기반 상품 목록 조회 테스트")
        void listLoad_category_happy() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            // when
            Slice<ProductListResponse> products = sut.getProductsByCategoryId(user1.getId(), "test", pageable);

            // then
            List<ProductListResponse> content = products.getContent();
            assertEquals(4, content.size());

            assertThat(content)
                    .allSatisfy(product -> assertThat(product.likedByMe()).isFalse());

        }

        @Test
        @DisplayName("[happy] 로그인 하지않은, 카테고리 기반 상품 목록 조회 테스트")
        void listLoad_category_user_null_happy() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            // when
            Slice<ProductListResponse> products = sut.getProductsByCategoryId(null, "test", pageable);

            // then
            List<ProductListResponse> content = products.getContent();
            assertEquals(4, content.size());

            assertThat(content)
                    .allSatisfy(product -> assertThat(product.likedByMe()).isFalse());

        }

        @Test
        @DisplayName("[unhappy] 카테고리 기반, 상품 예외 처리 조회 테스트")
        public void listLoad_category_throw_() throws Exception{

            //given

            //when

            //then
        }
    }

    /**
     * 카테고리 + 제조사 테스트
     */
    @Nested
    @DisplayName("카테고리+제조사 기반 목록 조회 서비스")
    class listLoadTest_category_manufacturer {

        @Test
        @DisplayName("[happy] 로그인한 유저 북마크 한, 카테고리+제조사 기반 상품 목록 조회 테스트")
        void listLoad_category_manufacturer_user_bookmark_happy() {

            // given
            Pageable pageable = PageRequest.of(0, 10);

            // 상품1,3에 북마크 설정
            var request1 = getBookmarkRequest(user1.getId(), product2.getId());
            var request2 = getBookmarkRequest(user1.getId(), product4.getId());

            List.of(request1, request2)
                    .forEach(req -> bookmarkService.saveBookmark(req));

            // when
            Slice<ProductListResponse> products = sut.getProductsByCategoryIdAndManufacturerId(
                    user1.getId(), "test", List.of("testManufacturer"), pageable);

            // then
            List<ProductListResponse> content = products.getContent();
            Assertions.assertEquals(1, content.size());

            content.forEach(product -> {
                if (product.id().equals(product2.getId()) || product.id().equals(product4.getId())) {
                    Assertions.assertTrue(product.likedByMe());
                }
                else {
                    Assertions.assertFalse(product.likedByMe());
                }
            });

        }



        @Test
        @DisplayName("[happy] 로그인한 유저 북마크 하지않은, 카테고리+제조사 기반 상품 목록 조회 테스트")
        void listLoad_category_manufacturer_happy() {

            // given
            Pageable pageable = PageRequest.of(0, 10);

            // when
            Slice<ProductListResponse> products = sut.getProductsByCategoryIdAndManufacturerId(
                    user1.getId(), "test", List.of("testManufacturer"), pageable);

            // then
            List<ProductListResponse> content = products.getContent();
            Assertions.assertEquals(1, content.size());

            assertThat(content)
                    .allSatisfy(product -> assertThat(product.likedByMe()).isFalse());

        }

        @Test
        @DisplayName("[happy] 로그인하지 않은 유저, 카테고리+제조사 기반 상품 목록 조회 테스트")
        void listLoad_category_manufacturer_user_null_happy() {

            // given
            Pageable pageable = PageRequest.of(0, 10);

            // when
            Slice<ProductListResponse> products = sut.getProductsByCategoryIdAndManufacturerId(
                    null, "test", List.of("testManufacturer"), pageable);

            // then
            List<ProductListResponse> content = products.getContent();
            Assertions.assertEquals(1, content.size());

            assertThat(content)
                    .allSatisfy(product -> assertThat(product.likedByMe()).isFalse());
        }




    }

    @Nested
    @DisplayName("카테고리+가격 기반 목록 조회 서비스")
    class listLoadTest_category_price {

        @Test
        @DisplayName("[happy] 로그인한 유저 북마크 한, 카테고리+금액 기반 상품 목록 조회 테스트")
        void listLoad_category_budget_user_bookmark_happy() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            double price = product1.getPrice();

            // 상품2에 북마크 설정
            var request1 = getBookmarkRequest(user1.getId(), product1.getId());

            bookmarkService.saveBookmark(request1);

            // when
            Slice<ProductListResponse> products = sut.getProductsByCategoryIdAndPrice(
                    user1.getId(), "test", (int) (price - 10000), (int) (price + 10000), pageable);

            List<ProductListResponse> content = products.getContent();

            List<String> strings = content.stream()
                    .map(ProductListResponse::productName)
                    .toList();

            // then
            Assertions.assertEquals(1, content.size());
            Assertions.assertEquals(strings.get(0), product1.getName());

            content.forEach(product -> {
                if (product.id().equals(product1.getId())) {
                    Assertions.assertTrue(product.likedByMe());
                }
                else {
                    Assertions.assertFalse(product.likedByMe());
                }
            });
        }

        @Test
        @DisplayName("[happy] 로그인한 유저 북마크 하지않은, 카테고리+금액 기반 상품 목록 조회 테스트")
        void listLoad_category_budget_happy() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            double price = product1.getPrice();

            // when
            Slice<ProductListResponse> products = sut.getProductsByCategoryIdAndPrice(
                    user1.getId(), "test", (int) (price - 10000), (int) (price + 10000), pageable);

            List<ProductListResponse> content = products.getContent();

            List<String> strings = content.stream()
                    .map(ProductListResponse::productName)
                    .toList();

            // then
            Assertions.assertEquals(1, content.size());
            Assertions.assertEquals(strings.get(0), product1.getName());

            assertThat(content)
                    .allSatisfy(product -> assertThat(product.likedByMe()).isFalse());
        }

        @Test
        @DisplayName("[happy] 로그인 하지않은, 카테고리+금액 기반 상품 목록 조회 테스트")
        void listLoad_category_budget_user_null_happy() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            double price = product1.getPrice();

            // when
            Slice<ProductListResponse> products = sut.getProductsByCategoryIdAndPrice(
                    null, "test", (int) (price - 10000), (int) (price + 10000), pageable);

            List<ProductListResponse> content = products.getContent();

            List<String> strings = content.stream()
                    .map(ProductListResponse::productName)
                    .toList();

            // then
            Assertions.assertEquals(1, content.size());
            Assertions.assertEquals(strings.get(0), product1.getName());

            assertThat(content)
                    .allSatisfy(product -> assertThat(product.likedByMe()).isFalse());
        }

    }

    /**
     * 카테고리+제조사+가격 기반 목록 조회 서비스
     */
    @Nested
    @DisplayName("카테고리+제조사+가격 기반 목록 조회 서비스")
    class listLoadTest_category_manufacturer_price {

        @Test
        @DisplayName("[happy] 로그인한 유저 북마크, 카테고리+제조사+가격 기반 상품 목록 조회 테스트")
        void listLoad_category_manufacturer_budget_user_bookmark_happy() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            int minPrice = (int) (product4.getPrice() - 10000);
            int maxPrice = (int) (product4.getPrice() + 10000);

            // 상품2에 북마크 설정
            var request1 = getBookmarkRequest(user1.getId(), product4.getId());

            bookmarkService.saveBookmark(request1);

            // when
            Slice<ProductListResponse> products = sut.getProductsByCategoryIdAndManufacturerIdAndPrice(
                    user1.getId(), "test", List.of("testManufacturer"), minPrice, maxPrice, pageable);

            // then
            List<ProductListResponse> content = products.getContent();
            Assertions.assertEquals(1, content.size());

            content.forEach(product -> {
                if (product.id().equals(product4.getId())) {
                    Assertions.assertTrue(product.likedByMe());
                }
                else {
                    Assertions.assertFalse(product.likedByMe());
                }
            });
        }

        @Test
        @DisplayName("[happy] 로그인한 유저 북마크 하지않은, 카테고리+제조사+가격 기반 상품 목록 조회 테스트")
        void listLoad_category_manufacturer_budget_happy() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            int minPrice = (int) (product4.getPrice() - 10000);
            int maxPrice = (int) (product4.getPrice() + 10000);

            // when
            Slice<ProductListResponse> products = sut.getProductsByCategoryIdAndManufacturerIdAndPrice(
                    user1.getId(), "test", List.of("testManufacturer"), minPrice, maxPrice, pageable);

            // then
            List<ProductListResponse> content = products.getContent();
            Assertions.assertEquals(1, content.size());

            assertThat(content)
                    .allSatisfy(product -> assertThat(product.likedByMe()).isFalse());
        }

        @Test
        @DisplayName("[happy] 로그인 하지 않은 유저, 카테고리+제조사+가격 기반 상품 목록 조회 테스트")
        void listLoad_category_manufacturer_budget_user_null_happy() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            int minPrice = (int) (product4.getPrice() - 10000);
            int maxPrice = (int) (product4.getPrice() + 10000);

            // when
            Slice<ProductListResponse> products = sut.getProductsByCategoryIdAndManufacturerIdAndPrice(
                    null, "test", List.of("testManufacturer"), minPrice, maxPrice, pageable);

            // then
            List<ProductListResponse> content = products.getContent();
            Assertions.assertEquals(1, content.size());

            assertThat(content)
                    .allSatisfy(product -> assertThat(product.likedByMe()).isFalse());
        }

    }

    /**
     * 공통 북마크 DTO 생성 함수
     * @param userId        유저 ID
     * @param productId     상품 ID
     */
    private BookmarkRequest getBookmarkRequest(UUID userId, String productId) {
        return BookmarkRequest.builder()
                .userId(userId)
                .productId(productId)
                .build();
    }


}

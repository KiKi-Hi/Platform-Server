package site.kikihi.custom.platform.application.service;

import site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark.BookmarkRequest;
import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocument;
import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocumentRepository;
import site.kikihi.custom.platform.application.in.recommendation.RecommendationUseCase;
import site.kikihi.custom.platform.application.out.bookmark.BookmarkPort;
import site.kikihi.custom.platform.application.out.user.UserPort;
import site.kikihi.custom.platform.domain.product.Product;
import site.kikihi.custom.platform.domain.product.ProductFixtures;
import site.kikihi.custom.platform.domain.user.User;
import site.kikihi.custom.platform.domain.user.UserFixtures;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class RecommendationServiceIntTest {

    @Autowired
    private RecommendationUseCase sut;

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private BookmarkPort bookmarkPort;

    /// 세팅을 위한 의존성
    @Autowired
    private ProductDocumentRepository repository;

    @Autowired
    private UserPort userPort;

    private ProductDocument product1;
    private ProductDocument product2;
    private ProductDocument product3;
    private ProductDocument product4;
    private ProductDocument product5;
    private ProductDocument product6;
    private ProductDocument product7;
    private ProductDocument product8;

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
        product5 = ProductFixtures.createProduct("test", "test5","testManufacturer",500000);
        product6 = ProductFixtures.createProduct("test", "test6","testManufacturer",600000);
        product7 = ProductFixtures.createProduct("test", "test7","testManufacturer",700000);
        product8 = ProductFixtures.createProduct("test", "test8","testManufacturer",800000);

        /// 유저 저장
        user1 = userPort.saveUser(UserFixtures.createUser(id1, "유저1", "socialId1"));
        user2 = userPort.saveUser(UserFixtures.createUser(id2, "유저2", "socialId2"));
        user3 = userPort.saveUser(UserFixtures.createUser(id3, "유저3", "socialId3"));

        /// DB에 모두 저장하기
        List<ProductDocument> documents = List.of(product1, product2, product3, product4, product5, product6, product7, product8);
        repository.saveAll(documents);
    }
    @AfterEach
    void tearDown() {
        repository.deleteAll();
        bookmarkPort.deleteAllBookmarks();
    }

    @Nested()
    @DisplayName("북마크에 따른 인기 순서 조회")
    class recommendation {

        @Test
        @DisplayName("[happy] 북마크 기반 추천 조회_전체_북마크_랜덤")
        public void loadRecommendation_preview() throws Exception {

            //given


            //when


            //then


        }

        @Test
        @DisplayName("[happy] 북마크 기반 추천 조회_전체_북마크_8개_이하")
        public void recommendation_under_8() throws Exception {
            // given
            /// 총 북마크 상품 2개 (< 8) -> 북마크 가중 2개, 새로운 랜덤 6개 추천
            bookmarkService.saveBookmark(user1.getId(), getBookmarkRequest(product1.getId()));
            bookmarkService.saveBookmark(user2.getId(), getBookmarkRequest(product1.getId()));
            bookmarkService.saveBookmark(user1.getId(), getBookmarkRequest(product2.getId()));

            Set<String> bookmarkedProductIds = Set.of(product1.getId(), product2.getId());

            // when
            List<Product> recommendation = sut.getProductsByRecommendation(null);

            // then
            Assertions.assertEquals(8, recommendation.size(), "총 8개의 상품이 추천되어야 합니다.");

            // 북마크 기반 2개 포함 검증
            long countFromBookmarks = recommendation.stream()
                    .map(Product::getId)
                    .filter(bookmarkedProductIds::contains)
                    .count();

            /// 북마크 상품이 최소 6개 이상 포함되는지 검증(랜덤 추가분에서 북마크 상품이 포함될 수 있음)
            Assertions.assertTrue(countFromBookmarks >= 2, "북마크 기반 추천 상품이 2개 이상 포함되어야 합니다.");
        }


        @Test
        @DisplayName("[happy] 북마크 기반 추천 조회_전체_북마크_20개_이하")
        public void recommendation_under_20() throws Exception {
            // given
            /// 북마크 가중 4개, 새로운 랜덤 4개 추천

            /// 새로운 상품 10개 목록 저장
            List<ProductDocument> products = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                products.add(ProductFixtures.createProduct("bulk", "bulk" + i, 1000));
            }
            List<ProductDocument> documents = repository.saveAll(products);

            /// 북마크 10개 저장 bookmarks
            for (ProductDocument p : documents) {
                bookmarkService.saveBookmark(user1.getId(), getBookmarkRequest(p.getId()));
                bookmarkService.saveBookmark(user2.getId(), getBookmarkRequest(p.getId()));
            }

            Set<String> bookmarkedProductIds = documents.stream()
                    .map(ProductDocument::getId)
                    .collect(Collectors.toSet());

            // when
            List<Product> recommendation = sut.getProductsByRecommendation(null);

            // then
            Assertions.assertEquals(8, recommendation.size(), "총 8개의 상품이 추천되어야 합니다.");

            // 북마크 기반 6개 포함 검증
            long countFromBookmarks = recommendation.stream()
                    .map(Product::getId)
                    .filter(bookmarkedProductIds::contains)
                    .count();

            /// 북마크 상품이 최소 4개 이상 포함되는지 검증(랜덤 추가분에서 북마크 상품이 포함될 수 있음)
            Assertions.assertTrue(countFromBookmarks >= 4, "북마크 기반 추천 상품이 4개 이상 포함되어야 합니다.");
        }

        @Test
        @DisplayName("[happy] 북마크 기반 추천 조회_전체_북마크_20_50개_사이")
        public void recommendation_20_50() throws Exception {
            // given
            /// 북마크 가중 6개, 새로운 랜덤 2개

            /// 기존 8 개 + 새로운 상품 30개 목록 저장
            List<ProductDocument> products = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                products.add(ProductFixtures.createProduct("bulk", "bulk" + i, 1000));
            }
            /// 저장
            List<ProductDocument> documents = repository.saveAll(products);

            /// 총 북마크 30개 (20 <= 30 <= 50)
            for (ProductDocument p : documents) {
                bookmarkService.saveBookmark(user1.getId(), getBookmarkRequest(p.getId()));
                bookmarkService.saveBookmark(user2.getId(), getBookmarkRequest(p.getId()));
            }

            /// 북마크를 한 상품 ID 목록
            List<String> bookmarkedProductIds = documents.stream()
                    .map(ProductDocument::getId)
                    .toList();

            // when
            List<Product> recommendation = sut.getProductsByRecommendation(null);

            // then
            Assertions.assertEquals(8, recommendation.size(), "총 8개의 상품이 추천되어야 합니다.");

            // 북마크 기반 6개 포함 검증
            long countFromBookmarks = recommendation.stream()
                    .map(Product::getId)
                    .filter(bookmarkedProductIds::contains)
                    .count();

            /// 북마크 상품이 최소 6개 이상 포함되는지 검증(랜덤 추가분에서 북마크 상품이 포함될 수 있음)
            Assertions.assertTrue(countFromBookmarks >= 6, "북마크 기반 추천 상품이 6개 이상 포함되어야 합니다.");
        }

        @Test
        @DisplayName("[happy] 북마크 기반 추천 조회_전체_북마크_50개_이상")
        public void recommendation_upper_50() throws Exception {
            // given
            /// 북마크 상위 50개 중 가중치 랜덤 8개

            // 테스트용 상품 60개 추가 생성 및 저장
            List<ProductDocument> products = new ArrayList<>();
            for (int i = 0; i < 60; i++) {
                products.add(ProductFixtures.createProduct("bulk", "bulk" + i, 1000));
            }

            /// 저장
            List<ProductDocument> documents = repository.saveAll(products);

            // 총 북마크 60개 (60 > 50)
            for (ProductDocument p : documents) {
                bookmarkService.saveBookmark(user1.getId(), getBookmarkRequest(p.getId()));
                bookmarkService.saveBookmark(user2.getId(), getBookmarkRequest(p.getId()));
                bookmarkService.saveBookmark(user3.getId(), getBookmarkRequest(p.getId()));
            }

            List<String> productIds = documents.stream()
                    .map(ProductDocument::getId)
                    .toList();

            // when
            List<Product> recommendation = sut.getProductsByRecommendation(null);

            // then
            Assertions.assertEquals(8, recommendation.size(), "총 8개의 상품이 추천되어야 합니다.");

            // 8개 모두 북마크된 상품인지 검증

            boolean allRecommendedAreBookmarked = recommendation.stream()
                    .map(Product::getId)
                    .allMatch(productIds::contains);
            Assertions.assertTrue(allRecommendedAreBookmarked, "추천된 모든 상품은 북마크된 상품이어야 합니다.");
        }

        @Test
        @DisplayName("[happy] 커스텀 기반 추천 조회")
        public void recommendation_preview() throws Exception {

        }


        @Test
        @DisplayName("[happy] 북마크 기반 추천 조회_전체_북마크_50개_이상_북마크 유저 수 기반 추천 가중치 빈도 테스트")
        void recommend_weightedByBookmarkUserCount() {
            // given
            /// 북마크 상위 50개 중 가중치 랜덤 8개

            // 테스트용 상품 60개 추가 생성 및 저장
            List<ProductDocument> products = new ArrayList<>();
            for (int i = 0; i < 60; i++) {
                products.add(ProductFixtures.createProduct("bulk", "bulk" + i, 1000));
            }

            /// 저장
            List<ProductDocument> documents = repository.saveAll(products);

            // 최소 총 북마크 60개
            for (ProductDocument p : documents) {
                bookmarkService.saveBookmark(user1.getId(), getBookmarkRequest(p.getId()));
            }

            /// 상품A: 100명 유저가 북마크
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                User u = userPort.saveUser(UserFixtures.createUser(UUID.randomUUID(), "userA" + i, "socialA" + i));
                users.add(u);
                bookmarkService.saveBookmark(u.getId(), getBookmarkRequest(product1.getId()));
            }

            /// 상품B: 2명이 북마크
            User userB1 = userPort.saveUser(UserFixtures.createUser(UUID.randomUUID(), "userB1", "socialB1"));
            User userB2 = userPort.saveUser(UserFixtures.createUser(UUID.randomUUID(), "userB2", "socialB2"));

            bookmarkService.saveBookmark(userB1.getId(),getBookmarkRequest( product2.getId()));
            bookmarkService.saveBookmark(userB2.getId(), getBookmarkRequest(product2.getId()));

            /// 상품C: 1명이 북마크
            User userC = userPort.saveUser(UserFixtures.createUser(UUID.randomUUID(), "userC1", "socialC1"));
            bookmarkService.saveBookmark(userC.getId(),getBookmarkRequest( product3.getId()));

            /// 제대로된 추천을 위해 8개의 상품에 대해서 진행해야된다.

            // when
            int repeatCount = 200;
            int countA = 0, countB = 0, countC = 0;
            for (int i = 0; i < repeatCount; i++) {
                List<Product> recs = sut.getProductsByRecommendation(null);
                List<String> ids = recs.stream()
                        .map(Product::getId)
                        .toList();
                if (ids.contains(product1.getId())) countA++;
                if (ids.contains(product2.getId())) countB++;
                if (ids.contains(product3.getId())) countC++;
            }

            // then
            /// 상품A(북마크 유저 100명) > 상품B(2명) > 상품C(1명)가 추천 빈도가 높아야 합니다.
            assertTrue(countA > countB && countB > countC,
                    String.format("가중치 추천 결과(유저당 1회 북마크): A:%d B:%d C:%d", countA, countB, countC));
        }


        @Test
        @DisplayName("[happy ]추천 상품 중복 없음 테스트")
        void recommendation_noDuplicates() {
            // given
            /// 북마크/랜덤 추천 데이터 준비(여러 상황에서 호출할 수 있으나, 추천 결과 유일성만 체크)

            bookmarkService.saveBookmark(user1.getId(),getBookmarkRequest(product1.getId()));
            bookmarkService.saveBookmark(user2.getId(), getBookmarkRequest(product1.getId()));
            bookmarkService.saveBookmark(user1.getId(), getBookmarkRequest(product2.getId()));

            /// when
            List<Product> recommended = sut.getProductsByRecommendation(null);

            // then
            /// 중복 없는지 검증: 추천 리스트와 distinct된 추천 리스트의 size가 같아야 함
            long uniqueCount = recommended.stream()
                    .map(Product::getId)
                    .distinct()
                    .count();

            Assertions.assertEquals(recommended.size(), uniqueCount, "추천 결과에 상품 중복이 없어야 합니다.");
        }

        /**
         * 공통 북마크 DTO 생성 함수
         *
         * @param productId 상품 ID
         */
        private BookmarkRequest getBookmarkRequest(String productId) {
            return BookmarkRequest.builder()
                    .productId(productId)
                    .build();
        }
    }

}

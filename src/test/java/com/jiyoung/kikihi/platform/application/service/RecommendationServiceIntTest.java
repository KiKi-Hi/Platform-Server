package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.BookmarkRequest;
import com.jiyoung.kikihi.platform.adapter.out.mongo.product.ProductDocument;
import com.jiyoung.kikihi.platform.adapter.out.mongo.product.ProductDocumentRepository;
import com.jiyoung.kikihi.platform.application.in.recommendation.RecommendationUseCase;
import com.jiyoung.kikihi.platform.application.out.bookmark.BookmarkPort;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.application.out.user.UserPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import com.jiyoung.kikihi.platform.domain.product.ProductFixtures;
import com.jiyoung.kikihi.platform.domain.user.User;
import com.jiyoung.kikihi.platform.domain.user.UserFixtures;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    @Autowired
    private ProductPort productPort;

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
    }

    @Nested()
    @DisplayName("북마크에 따른 인기 순서 조회")
    class recommendation {

        @Test
        @DisplayName("[happy] 추천 조회")
        public void loadRecommendation_preview() throws Exception {

            //given

            // 상품1에 3명의 유저가 북마크 설정
            var request1 = getBookmarkRequest(user1.getId(), product1.getId());
            var request2 = getBookmarkRequest(user2.getId(), product1.getId());
            var request3 = getBookmarkRequest(user3.getId(), product1.getId());

            // 상품 2에 2명의 유저가 북마크 설정
            var request4 = getBookmarkRequest(user1.getId(), product2.getId());
            var request5 = getBookmarkRequest(user2.getId(), product2.getId());

            // 상품 3에 1명의 유저가 북마크 설정
            var request6 = getBookmarkRequest(user3.getId(), product3.getId());

            // 상품 4에 0명의 유저가 북마크 설정

            // 북마크 저장
            List.of(request1, request2, request3, request4, request5, request6)
                    .forEach(req -> bookmarkService.saveBookmark(req));

            //when
            List<Product> recommendation = sut.getProductsByRecommendation();

            //then
            // 추천 상품 개수 검증
            Assertions.assertEquals(8, recommendation.size());

            // 추천 순서(북마크 개수 내림차순) 검증
            Assertions.assertEquals(product1.getId(), recommendation.get(0).getId()); // 3명
            Assertions.assertEquals(product2.getId(), recommendation.get(1).getId()); // 2명
            Assertions.assertEquals(product3.getId(), recommendation.get(2).getId()); // 1명

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

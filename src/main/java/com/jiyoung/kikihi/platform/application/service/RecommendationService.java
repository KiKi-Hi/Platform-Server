package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.application.in.recommendation.RecommendationUseCase;
import com.jiyoung.kikihi.platform.application.out.bookmark.BookmarkPort;
import com.jiyoung.kikihi.platform.application.out.bookmark.dto.TopBookmark;
import com.jiyoung.kikihi.platform.application.out.custom.CustomKeyboardPort;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.domain.custom.CustomKeyboard;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendationService implements RecommendationUseCase {

    /// 북마크 및 상품에서 정보 조회
    private final BookmarkPort bookmarkPort;
    private final ProductPort productPort;

    /// 커스텀 의존성 조회
    private final CustomKeyboardPort customPort;

    /// 추천 기준
    private static final int RECOMMEND_COUNT = 8;
    private static final int BOOKMARK_BOUND_1 = 20;
    private static final int BOOKMARK_BOUND_2 = 50;

    /**
     * 북마크 많은 순서 상위 N개(기본: 50개) 내에서 북마크 수 기반 가중치 확률 랜덤으로 M개 추천합니다.
     * - 커스텀 상품을 제작했을 경우, 커스텀 상품 위주로 추천을 진행합니다.
     * - 북마크 상품 = 0개 : 전체 랜덤
     * - 북마크 상품 < 8개 : 북마크 가중 2개 + 6개는 새로운 랜덤 상품
     * - 북마크 상품 < 20개: 북마크 가중 랜덤 4개 + 4개는 새로운 랜덤 상품
     * - 북마크 상품 20~50개 : 북마크 가중 6개 + 2개는 랜덤 상품
     * - 북마크 상품 > 50개: 북마크 상위 50개 내에서 북마크수 가중치 랜덤
     * @param userId 유저 ID (없으면 null)
     * @return 추천 상품 리스트
     */
    @Override
    public List<Product> getProductsByRecommendation(UUID userId) {

        /// 유저가 있다면 체크, 없다면 바로 북마크 조회
        if (userId != null) {

            /// 커스텀을 제작했다면, 비슷한 특성의 상품들을 추천
            Optional<CustomKeyboard> customKeyboard = customPort.loadCustomKeyBoardByUserId(userId);

            if (customKeyboard.isPresent()) {
                return recommendForCustomUser(customKeyboard.get());
            }
        }

        /// 북마크되어있는 상품의 사이즈 조회
        Long bookmarkProductSize = bookmarkPort.countBookmarks();

        /// 커스텀을 만들지 않았다면,
        if (bookmarkProductSize == 0) {
            /// 북마크 없음: 전체 랜덤 추천
            return getRandomProducts();
        } else if (bookmarkProductSize < RECOMMEND_COUNT) {
            /// 8개 미만 : 북마크 기반(가중치) 2개 + 랜덤 K개
            return recommendForFewBookmarks(bookmarkProductSize);
        }
        else if (bookmarkProductSize < BOOKMARK_BOUND_1) {
            /// 20개 미만: 북마크 기반(가중치) 4개 + 랜덤 4개
            return recommendForSmallBookmarks(bookmarkProductSize);
        } else if (bookmarkProductSize <= BOOKMARK_BOUND_2) {
            /// 20-50개: 북마크 기반(가중치) 6개 + 랜덤 2개
            return recommendForRegularBookmarks(bookmarkProductSize);
        } else {
            /// 51개 이상: 상위 50개 내에서 가중치 랜덤 비복원 8개
            return recommendForManyBookmarks();
        }

    }

    // =================
    //  내부 함수
    // =================

    /**
     * 추천상품 전체 랜덤으로 추천하는 내부 로직
     */
    private List<Product> getRandomProducts() {
        /// 전체 상품에서 랜덤으로 조회
        return productPort.getProductsRandomly(RECOMMEND_COUNT);
    }

    /**
     * 8개 미만: 북마크 기반(가중치) 2개 + 랜덤 6개
     * @param bookmarkSize  북마크 사이즈
     */
    private List<Product> recommendForFewBookmarks(Long bookmarkSize) {

        /// 북마크 개수 만큼 상위 인기 상품 리스트
        int bookmarkPick = Math.min(2, bookmarkSize.intValue()); // 북마크 2개까지, 실제 상품 수보다 많지 않게

        /// 조회
        List<TopBookmark> bookmarks = bookmarkPort.listTopBookmarks(bookmarkSize.intValue());

        /// 북마크 수 기준 랜덤 셀렉션
        List<Product> products = weightedRandomProductsFromBookmarks(bookmarks, bookmarkPick);

        /// 추천 상품ID 모음 (중복 추천 방지)
        List<String> excludeIds = getProductIds(products);

        /// 남은 수 만큼 랜덤 추천
        int rest = RECOMMEND_COUNT - products.size();
        List<Product> randoms = productPort.getRandomProductsExcludeIds(excludeIds, rest);

        /// 결과 합치기
        products.addAll(randoms);

        return products;
    }


    /**
     * 20개 미만: 북마크 기반(가중치) 4개 + 랜덤 4개
     * @param bookmarkSize  북마크 사이즈
     */
    private List<Product> recommendForSmallBookmarks(Long bookmarkSize) {

        /// 상위 N개 조회
        List<TopBookmark> topN = bookmarkPort.listTopBookmarks(Math.toIntExact(bookmarkSize));

        /// 가중치 바탕으로 조회
        List<Product> products = weightedRandomProductsFromBookmarks(topN, 4);

        /// 이미 존재하는 것 빼고 조회
        List<String> excludeIds = getProductIds(products);

        /// 랜덤으로 조회
        List<Product> randomly = productPort.getRandomProductsExcludeIds(excludeIds,4);

        products.addAll(randomly);
        return products;
    }

    /**
     * 북마크 기반(가중치) 6개 + 랜덤 2개
     * @param bookmarkSize  북마크 사이즈
     */
    private List<Product> recommendForRegularBookmarks(Long bookmarkSize) {

        /// 상위 N개 조회
        List<TopBookmark> topN = bookmarkPort.listTopBookmarks(Math.toIntExact(bookmarkSize));

        /// 가중치 바탕으로 조회
        List<Product> products = weightedRandomProductsFromBookmarks(topN, 6);

        /// 이미 존재하는 것 빼고 조회
        List<String> excludeIds = getProductIds(products);

        /// 랜덤으로 조회
        List<Product> randomly = productPort.getRandomProductsExcludeIds(excludeIds,2);

        products.addAll(randomly);
        return products;
    }

    /**
     * 상위 50개 내에서 가중치 랜덤으로 8개로 추천하는 내부 로직
     */
    private List<Product> recommendForManyBookmarks() {

        /// 북마크 상위 TOP 50개 조회
        List<TopBookmark> top50 = bookmarkPort.listTopBookmarks(50);

        /// 가중치 바탕으로 조회
        return weightedRandomProductsFromBookmarks(top50, RECOMMEND_COUNT);
    }

    /**
     * 커스텀 상품을 만든 유저에게 추천하는 내부 로직
     */
    private List<Product> recommendForCustomUser(CustomKeyboard customKeyboard) {
        return List.of();
    }


    // =================
    //  공통 함수
    // =================

    /**
     * 북마크 상품에서 count(가중치) 기반으로 N개 랜덤 추천.
     * @param bookmarks 북마크 목록(productId, count)
     * @param pickCount 뽑을 개수
     * @return 선정 상품 리스트
     */
    private List<Product> weightedRandomProductsFromBookmarks(List<TopBookmark> bookmarks, int pickCount) {

        /// 북마크가 없다면 빈 값
        if (bookmarks.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> pool = new ArrayList<>();

        /// for 반복문 실행
        for (TopBookmark bookmark: bookmarks) {
            for (int i = 0; i < bookmark.count(); i++) {

                /// 좋아요 수 만큼 해당 북마크 ID를 가중치 더하기
                pool.add(bookmark.productId());
            }
        }

        /// 랜덤으로 조회
        Collections.shuffle(pool);

        /// 중복 없이 고르기 위해서 Set함수 사용
        Set<String> pickedIds = new LinkedHashSet<>();
        Random rand = new Random();

        /// 개수만큼 골라지도록
        while (pickedIds.size() < pickCount && !pool.isEmpty()) {
            String pid = pool.get(rand.nextInt(pool.size()));
            pickedIds.add(pid);

            /// 뽑은 아이디는 삭제한다
            pool.removeIf(id -> id.equals(pid));
        }

        /// Product 객체로 매핑, 순서 보장
        List<Product> result = new ArrayList<>();
        for (String pid : pickedIds) {
            Product product = loadProduct(pid);
            result.add(product);
        }
        return result;
    }

    /**
     * 상품을 가져오는 함수 입니다.
     * @param id    상품 ID
     */
    private Product loadProduct(String id) {
        return productPort.getProduct(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
    }

    /**
     * 상품 Id들을 가져오는 함수 입니다.
     * @param products  상품 ID
     */
    private static List<String> getProductIds(List<Product> products) {
        return products.stream()
                .map(Product::getId)
                .toList();
    }
}

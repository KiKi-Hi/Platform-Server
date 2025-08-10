package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.application.in.recommendation.RecommendationUseCase;
import com.jiyoung.kikihi.platform.application.out.bookmark.BookmarkPort;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendationService implements RecommendationUseCase {

    /// 북마크 및 상품에서 정보 조회
    private final BookmarkPort bookmarkPort;
    private final ProductPort productPort;

    /**
     * 북마크에 따른 상품 추천 기능 구현
     */
    @Override
    public List<Product> getProductsByRecommendation() {
        Map<String, Long> favoriteBookmarks = bookmarkPort.getFavoriteBookmarks();
        List<Product> products = new ArrayList<>();

        // 북마크 많은 순서대로 최대 8개 추출
        List<String> topProductIds = favoriteBookmarks.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(8)
                .map(Map.Entry::getKey)
                .toList();

        // 북마크 상품 로딩
        for (String productId : topProductIds) {
            products.add(loadProduct(productId));
        }

        int remainCount = 8 - products.size();
        if (remainCount > 0) {
            // 이미 조회된 productId 제외하고 랜덤 상품 ID 조회
            List<String> excludedIds = new ArrayList<>(topProductIds);
            List<Product> randomProducts = productPort.getRandomProductsExcludeIds(excludedIds, remainCount);
            products.addAll(randomProducts);
        }

        return products;
    }


    /**
     * 상품을 가져오는 함수 입니다.
     * @param id    상품 ID
     */
    private Product loadProduct(String id) {
        return productPort.getProduct(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
    }
}

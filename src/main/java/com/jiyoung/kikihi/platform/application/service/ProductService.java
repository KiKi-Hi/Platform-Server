package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductDetailResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductListResponse;
import com.jiyoung.kikihi.platform.application.in.product.ProductUseCase;
import com.jiyoung.kikihi.platform.application.out.bookmark.BookmarkPort;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.domain.bookmark.Bookmark;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductPort productPort;

    /// 북마크 의존성 처리
    private final BookmarkPort bookmarkPort;

    // 상품 목록 조회
    /**
     * 최신 상품 목록 조회
     */
    @Override
    public List<Product> getProducts() {
        return productPort.getProducts();
    }

    /**
     * 카테고리별 목록 조회 (카테고리 포함)
     * 유저가 로그인했다면, 북마크한 내용까지 확인 되어야합니다.
     *
     * @param userId        유저 ID
     * @param categoryId    카테고리 ID
     * @param pageable      페이지
     */
    @Override
    public Slice<ProductListResponse> getProductsByCategoryId(UUID userId, String categoryId, Pageable pageable) {

        /// Port에서 조회
        Slice<Product> products = productPort.getProducts(categoryId, pageable);

        /// 공통 함수 바탕으로 처리
        return toProductListResponse(userId, categoryId, products);
    }

    /**
     * 카테고리별 목록 조회 (카테고리, 제조사 포함)
     * 유저가 로그인했다면, 북마크한 내용까지 확인 되어야합니다.
     *
     * @param categoryId        카테고리 ID
     * @param manufacturers     조회할 제조사
     * @param pageable          페이지
     */
    @Override
    public Slice<ProductListResponse> getProductsByCategoryIdAndManufacturerId(UUID userId, String categoryId, List<String> manufacturers, Pageable pageable) {

        /// Port에서 조회
        Slice<Product> products = productPort.getProducts(categoryId, manufacturers, pageable);

        /// 공통 함수 바탕으로 처리
        return toProductListResponse(userId, categoryId, products);
    }

    //

    /**
     * 카테고리별 목록 조회 (카테고리, 가격 포함)
     * 유저가 로그인했다면, 북마크한 내용까지 확인 되어야합니다.
     *
     * @param categoryId    카테고리 ID
     * @param minPrice      최소 가격
     * @param maxPrice      최대 가격
     * @param pageable      페이지
     */
    @Override
    public Slice<ProductListResponse> getProductsByCategoryIdAndPrice(UUID userId, String categoryId, Integer minPrice, Integer maxPrice, Pageable pageable) {

        /// Port에서 조회
        Slice<Product> products = productPort.getProducts(categoryId, minPrice, maxPrice, pageable);

        /// 공통 함수 바탕으로 처리
        return toProductListResponse(userId, categoryId, products);
    }


    /**
     * 카테고리별 목록 조회 (카테고리, 제조사, 가격 포함)
     * 유저가 로그인했다면, 북마크한 내용까지 확인 되어야합니다.
     *
     * @param categoryId        카테고리 ID
     * @param manufacturers     제조사
     * @param minPrice          최소 가격
     * @param maxPrice          최대 가격
     * @param pageable          페이지
     */
    @Override
    public Slice<ProductListResponse> getProductsByCategoryIdAndManufacturerIdAndPrice(UUID userId, String categoryId, List<String> manufacturers, Integer minPrice, Integer maxPrice, Pageable pageable) {

        /// Port에서 조회
        Slice<Product> products = productPort.getProducts(categoryId, manufacturers, minPrice, maxPrice, pageable);

        /// 공통 함수 바탕으로 처리
        return toProductListResponse(userId, categoryId, products);
    }

    // 상품 상세 조회

    /**
     * 상품 상세 조회
     *
     * @param id        조회할 상품 ID
     * @param userId    유저 아이디
     */
    @Override
    public ProductDetailResponse getProduct(UUID userId, String id) {

        /// 상품 조회
        Product product = loadProduct(id);

        /// 로그인 하지 않은 유저가 확인한다면
        if (userId == null) {
            return ProductDetailResponse.from(product);
        }

        /// 북마크 의존성 추가
        boolean bookmark = bookmarkPort.checkBookmarkByProductIdAndUserId(id, userId);

        // TODO! 배송정보, 추천 아이템, 등등 추가로 설정하기

        /// DTO 변환
        return ProductDetailResponse.from(product, bookmark);

    }

    // 상품 추천 목록 조회

    /**
     * 추천 서비스를 구현 합니다!
     * - 북마크가 많은 순서대로 추천합니다.
     */
    @Override
    public List<Product> getProductsByRecommendation() {

        /// 인기 있는 북마크 상품 조회
        // Map <ProductId, 북마크 개수>
        Map<String, Long> favoriteBookmarks = bookmarkPort.getFavoriteBookmarks();

        /// 상품 ID 바탕으로 8개 조회
        List<String> topProductIds = favoriteBookmarks.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(8)
                .map(Map.Entry::getKey)
                .toList();

        /// TODO! 순서를 보장한 상태로 한번에 가져오는 방안 구상하기

        /// 상품 목록 가져오기
        List<Product> products = new ArrayList<>();

        /// 상품 ID 바탕으로 조회, 순서대로 조회해서 정렬 유지
        topProductIds.forEach(productId -> {
            Product product = loadProduct(productId);
            products.add(product);
        });

        return products;

    }

    // 공통 함수
    /**
     * 상품 목록 조회를 진행할때, 북마크 여부를 파악하는 함수입니다.
     * @param userId        유저 ID
     * @param categoryId    카테고리 ID
     * @param products      상품 목록
     */
    private Slice<ProductListResponse> toProductListResponse(UUID userId, String categoryId, Slice<Product> products) {
        /// 응답 값
        List<ProductListResponse> dtoList;

        /// 상품 목록 꺼내서 DTO 변환
        List<Product> content = products.getContent();

        /// 로그인 하지 않은 유저가 확인한다면
        if (userId == null) {

            /// 하트가 전부 false 되는 로직
            dtoList = ProductListResponse.from(content);

            /// 새로운 Slice 객체로 생성
            return new SliceImpl<>(dtoList, products.getPageable(), products.hasNext());
        }

        /// 유저가 북마크를 했는지 체크
        List<Bookmark> bookmarks = bookmarkPort.getBookmarksByUserIdAndCategoryId(userId, categoryId);

        /// 북마크된 상품 ID만 추출
        Set<String> bookmarkedProductIds = bookmarks.stream()
                .map(Bookmark::getProductId)
                .collect(Collectors.toSet());

        // 북마크 여부 반영하여 DTO 변환
        dtoList = ProductListResponse.from(content, bookmarkedProductIds);

        return new SliceImpl<>(dtoList, products.getPageable(), products.hasNext());
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

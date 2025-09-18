package site.kikihi.custom.platform.application.service;

import org.springframework.data.domain.*;
import site.kikihi.custom.global.response.ErrorCode;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductDetailResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.application.in.product.ProductUseCase;
import site.kikihi.custom.platform.application.out.bookmark.BookmarkPort;
import site.kikihi.custom.platform.application.out.product.ProductPort;
import site.kikihi.custom.platform.domain.bookmark.Bookmark;
import site.kikihi.custom.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 상품 서비스
 * - 목록 조회 기능을 수행합니다.
 * - 상세 조회 기능을 수행합니다.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    /// 상품 DB 관련 처리
    private final ProductPort productPort;

    /// 북마크 의존성 처리
    private final BookmarkPort bookmarkPort;

    // =================
    //  상품 목록 조회
    // =================

    /**
     * 카테고리별 목록 조회 (카테고리 포함)
     * 유저가 로그인했다면, 북마크한 내용까지 확인 되어야합니다.
     *
     * @param userId        유저 ID
     * @param categoryId    카테고리 ID
     * @param pageable      페이지
     */
    @Override
    public Page<ProductListResponse> getProductsByCategoryId(UUID userId, String categoryId, Pageable pageable) {

        /// Port에서 조회
        Page<Product> products = productPort.getProducts(categoryId, pageable);

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
    public Page<ProductListResponse> getProductsByCategoryIdAndManufacturerId(UUID userId, String categoryId, List<String> manufacturers, Pageable pageable) {

        /// Port에서 조회
        Page<Product> products = productPort.getProducts(categoryId, manufacturers, pageable);

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
    public Page<ProductListResponse> getProductsByCategoryIdAndPrice(UUID userId, String categoryId, Integer minPrice, Integer maxPrice, Pageable pageable) {

        /// Port에서 조회
        Page<Product> products = productPort.getProducts(categoryId, minPrice, maxPrice, pageable);

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
    public Page<ProductListResponse> getProductsByCategoryIdAndManufacturerIdAndPrice(UUID userId, String categoryId, List<String> manufacturers, Integer minPrice, Integer maxPrice, Pageable pageable) {

        /// Port에서 조회
        Page<Product> products = productPort.getProducts(categoryId, manufacturers, minPrice, maxPrice, pageable);

        /// 공통 함수 바탕으로 처리
        return toProductListResponse(userId, categoryId, products);
    }

    // =================
    //  상품 상세 조회
    // =================

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
        boolean bookmark = bookmarkPort.checkBookmarkByUserIdAndProductId(userId, id);

        // TODO! 배송정보, 추천 아이템, 등등 추가로 설정하기

        /// DTO 변환
        return ProductDetailResponse.from(product, bookmark);

    }

    /**
     * 모든 제조사를 가져오는 로직입니다.
     * @param categoryId    조회할 카테고리 ID
     */
    @Override
    public Slice<String> getManufacturers(String categoryId, Pageable pageable) {

        /// Port에서 일단 전부 조회
        // TODO! DB가 적기 때문에, 일단은 다 가져와서 슬라이싱,,, 추후에 데이터의 개수가 많아지게 된다면 로직을 다시 생각해야 될 듯
        List<String> manufacturers = productPort.getManufacturers(categoryId);

        // 페이징 적용
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), manufacturers.size());
        List<String> content = manufacturers.subList(start, end);

        boolean hasNext = end < manufacturers.size();

        return new SliceImpl<>(content, pageable, hasNext);
    }

    // ========================
    // 외부 의존성

    // =================
    //  공통 함수
    // =================
    /**
     * 상품 목록 조회를 진행할때, 북마크 여부를 파악하는 함수입니다.
     * @param userId        유저 ID
     * @param categoryId    카테고리 ID
     * @param products      상품 목록
     */
    private Page<ProductListResponse> toProductListResponse(UUID userId, String categoryId, Page<Product> products) {
        /// 응답 값
        List<ProductListResponse> dtoList;

        /// 상품 목록 꺼내서 DTO 변환
        List<Product> content = products.getContent();

        /// 로그인 하지 않은 유저가 확인한다면
        if (userId == null) {

            /// 하트가 전부 false 되는 로직
            dtoList = ProductListResponse.from(content);

            /// 새로운 Slice 객체로 생성
            return new PageImpl<>(dtoList, products.getPageable(), products.getTotalElements());
        }

        /// 유저가 북마크를 했는지 체크
        List<Bookmark> bookmarks = bookmarkPort.getBookmarksByUserIdAndCategoryId(userId, categoryId);

        /// 북마크된 상품 ID만 추출
        Set<String> bookmarkedProductIds = bookmarks.stream()
                .map(Bookmark::getProductId)
                .collect(Collectors.toSet());

        // 북마크 여부 반영하여 DTO 변환
        dtoList = ProductListResponse.from(content, bookmarkedProductIds);

        return new PageImpl<>(dtoList, products.getPageable(), products.getTotalElements());
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

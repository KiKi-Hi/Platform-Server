package site.kikihi.custom.platform.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import site.kikihi.custom.global.response.ErrorCode;
import site.kikihi.custom.platform.adapter.in.web.dto.request.custom.CustomKeyboardRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.request.product.CategoryType;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.application.in.custom.CustomKeyboardUseCase;
import site.kikihi.custom.platform.application.out.bookmark.BookmarkPort;
import site.kikihi.custom.platform.application.out.custom.CustomKeyboardPort;
import site.kikihi.custom.platform.application.out.product.ProductPort;
import site.kikihi.custom.platform.application.out.user.UserPort;
import site.kikihi.custom.platform.domain.bookmark.Bookmark;
import site.kikihi.custom.platform.domain.custom.CustomKeyboard;
import site.kikihi.custom.platform.domain.custom.CustomKeyboardLayout;
import site.kikihi.custom.platform.domain.custom.CustomKeyboardWithName;
import site.kikihi.custom.platform.domain.product.Product;
import site.kikihi.custom.platform.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 키보드 커스텀 서비스
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomKeyboardService implements CustomKeyboardUseCase {

    /// DB 포트
    private final CustomKeyboardPort port;
    private final ProductPort productPort;

    /// 외부 의존성 처리
    private final UserPort userPort;
    private final BookmarkPort bookmarkPort;

    // =================
    //  저장 함수
    // =================

    /**
     * 커스텀 키보드를 저장하는 로직
     *
     * @param request 키보드 저장 DTO
     */
    @Override
    public CustomKeyboard saveCustomKeyboard(CustomKeyboardRequest request, UUID userId) {

        /// 상품 예외 처리
        var frameProduct = getProduct(request.getHousingId());
        var switchProduct = getProduct(request.getSwitchId());
        var keyCapProduct = getProduct(request.getKeyCapId());

        /// 악세사리 (선택)
        String accessoryId = null;
        if (request.getAccessoryId() != null && !request.getAccessoryId().isBlank()) {
            var accessoryProduct = getProduct(request.getAccessoryId());
            accessoryId = accessoryProduct.getId();
        }

        /// 객체 생성
        var customKeyboard = CustomKeyboard.of(userId, request.getLayout(), frameProduct.getId(), switchProduct.getId(), keyCapProduct.getId(), accessoryId, request.getName(), "thumbnail");

        /// 저장 후 리턴
        return port.saveCustomKeyboard(customKeyboard);
    }


    /**
     * 상품 부품 추가하기
     * @param customKeyboardId  커스텀 키보드 ID
     * @param categoryId        추가할 카테고리
     * @param productId         추가할 상품 ID
     * @param userId            유저
     */
    @Override
    public void insertProductInCustomKeyboard(Long customKeyboardId, String categoryId, String productId, UUID userId) {

        /// 해당 유저의 커스텀 키보드인지 체크
        boolean checked = port.existCustomKeyboardByUserIdAndId(userId, customKeyboardId);

        /// 키보드가 요청자의 것이 아니라면 에러 발생
        if (!checked) {
            throw new IllegalStateException(ErrorCode.UNAUTHORIZED_DELETE_CUSTOM.getMessage());
        }

        /// 추가하고자 하는 상품이 존재하는지 체크
        Product product = getProduct(productId);

        /// 해당 유저의 상품이며, 커스텀 내부에 상품이 존재하기에 삭제 가능하다.
        port.addProductInsideCustomKeyboard(customKeyboardId, categoryId, product.getId());

    }

    // =================
    //  조회 함수
    // =================


    /**
     * 내가 만든 키보드 목록 조회
     * @param userId    유저 ID
     */
    @Override
    public Slice<CustomKeyboardWithName> getCustomKeyboards(UUID userId) {

        /// 유저 예외 처리
        User user = getUser(userId);

        /// 커스텀 키보드 목록 조회
        Slice<CustomKeyboard> keyboards = port.loadCustomKeyboardsByUserID(user.getId());

        /// 비었다면 빈 값 출력
        if (keyboards == null) {
            keyboards = new SliceImpl<>(Collections.emptyList());
        }

        /// 필요한 모든 productId를 한 번에 수집
        Set<String> allProductIds = keyboards.stream()
                .flatMap(k -> Stream.of(
                        k.getFrameId(),
                        k.getSwitchId(),
                        k.getKeyCapId(),
                        k.getAccessoryId() // null 포함 가능, 아래에서 안전 처리
                ))
                .filter(Objects::nonNull) // null 제거
                .collect(Collectors.toSet());


        /// productId로 한 번에 조회 (N+1 방지 핵심)
        Map<String, Product> productMap = getProductsByIds(allProductIds);

        /// 결과 변환
        List<CustomKeyboardWithName> customKeyboards = keyboards.stream()
                .map(keyboard -> {
                    var frameProduct = productMap.get(keyboard.getFrameId());
                    var switchProduct = productMap.get(keyboard.getSwitchId());
                    var keyCapProduct = productMap.get(keyboard.getKeyCapId());
                    var accessoryProduct = productMap.get(keyboard.getAccessoryId());

                    return CustomKeyboardWithName.of(keyboard, frameProduct, switchProduct, keyCapProduct, accessoryProduct);
                })
                .toList();

        return new SliceImpl<>(customKeyboards, keyboards.getPageable(), keyboards.hasNext());
    }



    /**
     * 커스텀 키보드 상세 조회하기
     * @param customKeyboardId  조회할 커스텀 키보드 ID
     */
    @Override
    public CustomKeyboardWithName getCustomKeyboard(Long customKeyboardId) {

        /// 예외 처리 후 응답,
        CustomKeyboard keyBoard = getKeyboard(customKeyboardId);

        /// 개별 상품 조회
        // TODO! 한번에 조회하도록 쿼리문 수정
        var frameProduct = getProduct(keyBoard.getFrameId());
        var switchProduct = getProduct(keyBoard.getSwitchId());
        var keyCapProduct = getProduct(keyBoard.getKeyCapId());

        String accessoryId = null;
        if (keyBoard.getAccessoryId() != null && !keyBoard.getAccessoryId().isBlank()) {

        }

        // 악세사리가 존재하면 조회
        var accessoryProduct = keyBoard.getAccessoryId() != null && !keyBoard.getAccessoryId().isBlank()
                ? getProduct(keyBoard.getAccessoryId())
                : null;

        /// 응답
        return CustomKeyboardWithName.of(keyBoard, frameProduct, switchProduct, keyCapProduct, accessoryProduct);

    }

    /**
     * 키보드 배열을 바탕으로 가능한 상품 목록 조회 (카테고리)
     *
     * @param userId   유저ID
     * @param type     조회할 배열 타입
     * @param pageable 페이징
     */
    @Override
    public Page<ProductListResponse> getCustomProducts(UUID userId, String categoryId, CustomKeyboardLayout type, Pageable pageable) {

        Page<Product> products;

        /// 타입을 바탕으로 조회하기
        /// 하우징인 경우
        if (categoryId.equals(CategoryType.HOUSING.getValue())){
            products = productPort.getCustomProducts(type.getDb(), categoryId, pageable);
        }
        /// 키캡인 경우
        else if (categoryId.equals(CategoryType.KEYCAP.getValue())) {
            products = productPort.getCustomProducts(categoryId, pageable);
        } else {
            products = productPort.getProducts(categoryId, pageable);
        }

        /// 북마크 여부도 파악하기
        return toProductListResponse(userId, categoryId, products);
    }

    /**
     * 키보드 배열을 바탕으로 상품 목록 조회 (카테고리,북마크)
     * @param userId        유저ID
     * @param categoryId    카테고리
     * @param type          조회할 타입
     * @param pageable      페이징
     */
    @Override
    public Page<ProductListResponse> getProductsByBookmark(UUID userId, String categoryId, CustomKeyboardLayout type, Pageable pageable) {
        Page<Product> products;

        /// 타입을 바탕으로 조회하기
        /// 하우징인 경우
        if (categoryId.equals(CategoryType.HOUSING.getValue())){
            products = productPort.getCustomProducts(type.getDb(), categoryId, pageable);
        }
        /// 키캡인 경우
        else if (categoryId.equals(CategoryType.KEYCAP.getValue())) {
            products = productPort.getCustomProducts(categoryId, pageable);
        } else {
            products = productPort.getProducts(categoryId, pageable);
        }

        /// 북마크 여부 파악해서 해당 상품만 가져오기
        return toProductBookmarkResponse(userId, categoryId, products);
    }

    /**
     * 키보드 배열을 바탕으로 상품 목록의 필터링 조회 (카테고리, 가격)
     * @param userId     유저 ID
     * @param categoryId 카테고리 ID
     * @param minPrice   최소가격
     * @param maxPrice   최대가격
     * @param pageable   페이징
     */
    @Override
    public Page<ProductListResponse> getProductsByCategoryIdAndPrice(UUID userId, String categoryId, CustomKeyboardLayout type, Integer minPrice, Integer maxPrice, Pageable pageable) {

        /// Port에서 조회
        Page<Product> products;

        /// 하우징인 경우
        if (categoryId.equals(CategoryType.HOUSING.getValue())){
            products = productPort.getCustomProducts(type.getDb(), categoryId, minPrice, maxPrice, pageable);
        }
        /// 키캡인 경우
        else if (categoryId.equals(CategoryType.KEYCAP.getValue())) {
            products = productPort.getCustomProducts(categoryId, minPrice, maxPrice, pageable);

        } else {
            products = productPort.getProducts(categoryId, minPrice, maxPrice, pageable);
        }

        /// 북마크 여부 파악해서 해당 상품만 가져오기
        return toProductListResponse(userId, categoryId, products);
    }

    /**
     * 키보드 배열을 바탕으로 상품 목록의 필터링 조회 (카테고리,북마크,가격)
     *
     * @param userId     유저 ID
     * @param categoryId 카테고리 ID
     * @param minPrice   최소가격
     * @param maxPrice   최대가격
     * @param pageable   페이징
     */
    @Override
    public Page<ProductListResponse> getProductsByFilterAndBookmark(UUID userId, String categoryId, CustomKeyboardLayout type, Integer minPrice, Integer maxPrice, Pageable pageable) {
        /// Port에서 조회
        Page<Product> products;

        /// 하우징인 경우
        if (categoryId.equals(CategoryType.HOUSING.getValue())){
            products = productPort.getCustomProducts(type.getDb(), categoryId, minPrice, maxPrice, pageable);
        }
        /// 키캡인 경우
        else if (categoryId.equals(CategoryType.KEYCAP.getValue())) {
            products = productPort.getCustomProducts(categoryId, minPrice, maxPrice, pageable);

        } else {
            products = productPort.getProducts(categoryId, minPrice, maxPrice, pageable);
        }

        /// 북마크 여부 파악해서 해당 상품만 가져오기
        return toProductBookmarkResponse(userId, categoryId, products);
    }


    // =================
    //  삭제 함수
    // =================

    /**
     * 커스텀 키보드 삭제
     * @param customKeyboardId 삭제할 커스텀 ID
     * @param userId            삭제하는 유저 ID
     */
    @Override
    public void deleteCustomKeyboard(Long customKeyboardId, UUID userId) {

        /// 해당 유저의 커스텀 키보드인지 체크
        boolean checked = port.existCustomKeyboardByUserIdAndId(userId, customKeyboardId);

        /// 키보드가 요청자의 것이 아니라면 에러 발생
        if (!checked){
            throw new IllegalStateException(ErrorCode.UNAUTHORIZED_DELETE_CUSTOM.getMessage());
        }

        /// 권한이 정상이기에 삭제
        port.deleteCustomKeyboard(customKeyboardId);

    }

    /**
     * 커스텀 키보드 내부에 있는 부품을 삭제합니다
     *
     * @param customKeyboardId 삭제할 커스텀 ID
     * @param categoryId       카테고리 ID
     * @param productId        부품 ID
     * @param userId           삭제하는 유저 ID
     */
    @Override
    public void deleteCustomInside(Long customKeyboardId, String categoryId, String productId, UUID userId) {

        /// 해당 유저의 커스텀 키보드인지 체크
        boolean checked = port.existCustomKeyboardByUserIdAndId(userId, customKeyboardId);

        /// 키보드가 요청자의 것이 아니라면 에러 발생
        if (!checked) {
            throw new IllegalStateException(ErrorCode.UNAUTHORIZED_DELETE_CUSTOM.getMessage());
        }

        /// 커스텀 내부에 해당 상품의 ID가 있는지 여부 확인
        boolean existed = port.existProductInsideCustomKeyboard(customKeyboardId, categoryId, productId);

        /// 키보드에 상품이 없었다면 삭제
        if (!existed) {
            throw new IllegalStateException(ErrorCode.BAD_PRODUCT_DELETE_CUSTOM.getMessage());
        }

        /// 해당 유저의 상품이며, 커스텀 내부에 상품이 존재하기에 삭제 가능하다.
        port.deleteProductInsideCustomKeyboard(customKeyboardId, categoryId, productId);

    }


    // =================
    //  공통 함수
    // =================

    /**
     * 상품 조회 함수
     * @param productId 상품 ID
     */
    private Product getProduct(String productId) {
        return productPort.getProduct(productId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
    }

    /**
     * 상품 여러개 조회 함수
     * @param productIds   상품 IDs
     */
    private Map<String, Product> getProductsByIds(Set<String> productIds) {
        return productPort.getProductsByIds(new ArrayList<>(productIds));
    }



    /**
     * 유저 조회 함수
     * @param userId  유저 ID
     */
    private User getUser(UUID userId) {
        return userPort.loadUserById(userId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    /**
     * 커스텀 키보드 조회 함수
     * @param customKeyboardId  커스텀 키보드 ID
     */
    private CustomKeyboard getKeyboard(Long customKeyboardId) {
        return port.loadCustomKeyboard(customKeyboardId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.CUSTOM_NOT_FOUND.getMessage()));
    }

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

    // =================
    //  공통 함수
    // =================
    /**
     * 상품 목록 조회를 진행할때, 북마크 여부를 파악하는 함수입니다.
     * @param userId        유저 ID
     * @param categoryId    카테고리 ID
     * @param products      상품 목록
     */
    private Page<ProductListResponse> toProductBookmarkResponse(UUID userId, String categoryId, Page<Product> products) {
        /// 응답 값
        List<ProductListResponse> dtoList;

        /// 상품 목록 꺼내서 DTO 변환
        List<Product> content = products.getContent();

        /// 로그인 하지 않은 유저가 확인한다면
        if (userId == null) {

            dtoList = List.of();

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
        dtoList = ProductListResponse.fromBookmark(content, bookmarkedProductIds);

        return new PageImpl<>(dtoList, products.getPageable(), products.getTotalElements());
    }

}

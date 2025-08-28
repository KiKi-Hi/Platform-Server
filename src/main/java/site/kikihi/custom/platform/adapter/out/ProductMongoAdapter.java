package site.kikihi.custom.platform.adapter.out;

import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocument;
import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocumentRepository;
import site.kikihi.custom.platform.application.out.product.ProductPort;
import site.kikihi.custom.platform.domain.custom.CustomKeyboardLayout;
import site.kikihi.custom.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MongoDB 와 ElasticSearch 통해 기능 구현
 */

@Component
@RequiredArgsConstructor
public class ProductMongoAdapter implements ProductPort {

    private final ProductDocumentRepository documentRepository;

    // =================
    //  상품 상세 조회
    // =================

    /**
     * 상품 상세 조회
     * @param productId 상품 ID
     */
    @Override
    public Optional<Product> getProduct(String productId) {

        /// DB 조회
        return documentRepository.findById(productId)
                .map(ProductDocument::toDomain);
    }

    // =================
    //  상품 목록 조회
    // =================
    /// 카테고리 기반 상품 목록 조회 (카테고리만)
    @Override
    public Slice<Product> getProducts(String category, Pageable pageable) {

        /// DB 조회
        Slice<ProductDocument> result = documentRepository
                .findByCategory(category, pageable);

        return result.
                map(ProductDocument::toDomain);
    }

    /// 카테고리 기반 상품 목록 조회 (카테고리, 제조사 포함)
    @Override
    public Slice<Product> getProducts(String category, List<String> manufacturer, Pageable pageable) {

        /// DB 조회
        Page<ProductDocument> result = documentRepository
                .findByCategoryAndManufacturer(category, manufacturer, pageable);

        return result.
                map(ProductDocument::toDomain);
    }

    /// 카테고리 기반 상품 목록 조회 (카테고리, 가격 포함)
    @Override
    public Slice<Product> getProducts(String category, Integer minPrice, Integer maxPrice, Pageable pageable) {
        /// DB 조회
        Slice<ProductDocument> result = documentRepository
                .findByCategoryAndPriceRange(category, minPrice, maxPrice, pageable);

        return result.
                map(ProductDocument::toDomain);
    }

    /// 카테고리 기반 상품 목록 조회 (카테고리, 제조사, 가격 포함)
    @Override
    public Slice<Product> getProducts(String category, List<String> manufacturer,
                                     Integer minPrice, Integer maxPrice, Pageable pageable) {

        /// DB 조회
        Slice<ProductDocument> result = documentRepository
                .findByCategoryAndManufacturerAndPriceRange(category, manufacturer, minPrice, maxPrice, pageable);

        return result.
                map(ProductDocument::toDomain);
    }

    /// 카테고리 기반 제조사 목록 조회
    @Override
    public List<String> getManufacturers(String category) {

        /// DB 조회
        return documentRepository.findManufacturersByCategory(category);
    }

    // =================
    //  상품 조회
    // =================

    /**
     * 상품 아이디 기반 조회
     * @param productIds    상품Id
     * @param pageable      페이징
     */
    @Override
    public Slice<Product> getProductsByIds(List<String> productIds, Pageable pageable) {
        return documentRepository.findByIdIn(productIds, pageable)
                .map(ProductDocument::toDomain);
    }

    /**
     * 상품 아이디 기반 조회
     * @param productIds    상품Id
     */
    @Override
    public Map<String, Product> getProductsByIds(List<String> productIds) {
        return documentRepository.findByIdIn(productIds).stream()
                .map(ProductDocument::toDomain)
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    // =================
    //  상품 추천
    // =================

    /**
     * 특정 아이디 제외 랜덤 추천
     * @param excludedIds   제외할 아이디
     * @param limit         개수
     */
    @Override
    public List<Product> getRandomProductsExcludeIds(List<String> excludedIds, int limit) {
        return documentRepository.findRandomExcludeIds(excludedIds, limit).stream()
                .map(ProductDocument::toDomain)
                .toList();
    }

    /**
     * 랜덤 추천
     * @param limit 개수
     */
    @Override
    public List<Product> getProductsRandomly(int limit) {
        return documentRepository.findRandomProducts(limit).stream()
                .map(ProductDocument::toDomain)
                .toList();
    }

    /**
     * 비슷한 특성 추천
     * @param switchId  스위치
     * @param keycapId  키캡
     * @param layout    레이아웃
     */
    @Override
    public List<Product> findProductsByAttributes(String switchId, String keycapId, CustomKeyboardLayout layout) {
        return List.of();
    }

    // =================
    //  상품 삭제
    // =================
    @Override
    public void deleteProduct(String productId) {

    }



}

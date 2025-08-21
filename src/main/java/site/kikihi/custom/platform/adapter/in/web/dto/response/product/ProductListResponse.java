package site.kikihi.custom.platform.adapter.in.web.dto.response.product;


import site.kikihi.custom.platform.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.util.List;
import java.util.Set;

import static site.kikihi.custom.platform.adapter.in.web.dto.response.util.ProductPriceUtil.getPrice;

/**
 * 상품 목록 응답 DTO
 *
 * @param id                상품 아이디
 * @param thumbnail         썸네일 이미지 URL
 * @param category          카테고리
 * @param manufacturerName  제조사명
 * @param productName       제품명
 * @param likedByMe         나의 좋아요 여부 (true/false)
 * @param discountedPrice   가격 (기존 DTO 컬럼과 동일하게 유지)
 */

@Builder
@Schema(name = "[응답][상품] 상품 목록 조회 Response", description = "상품 목록 조회를 위한 DTO입니다.")
public record ProductListResponse(

        @Schema(description = "상품 아이디", example = "101")
        String id,

        @Schema(description = "상품 썸네일 이미지 URL", example = "https://example.com/product/101.jpg")
        String thumbnail,

        @Schema(description = "카테고리명", example = "keycap")
        String category,

        @Schema(description = "제조사명", example = "애플")
        String manufacturerName,

        @Schema(description = "제품명", example = "맥북 키보드")
        String productName,

        @Schema(description = "최저가 가격부터", example = "599000원 ~")
        String discountedPrice,

        @Schema(description = "내가 좋아요(북마크)한 상품 여부", example = "true")
        boolean likedByMe
) {

    /// 정적 팩토리 메서드
    public static ProductListResponse from(Product product) {
        return ProductListResponse.builder()
                .id(product.getId())
                .thumbnail(product.getThumbnail())
                .category(product.getCategory())
                .manufacturerName(product.getManufacturer())
                .productName(product.getName())
                .discountedPrice(getPrice(product))
                .likedByMe(false)
                .build();
    }

    /// 정적 팩토리 메서드
    public static List<ProductListResponse> from(List<Product> products) {
        return products.stream()
                .map(ProductListResponse::from)
                .toList();
    }

    /// 북마크한 내용이 있을 때, 정적 팩토리 메서드
    public static List<ProductListResponse> from(List<Product> products, Set<String> bookmarkProductIds) {
        return products.stream()
                .map(product -> ProductListResponse.from(
                        product,
                        bookmarkProductIds.contains(product.getId())))
                .toList();
    }

    /// 북마크한 내용이 있을 때 사용하는, 내부 정적 팩토리 메서드
    public static ProductListResponse from(Product product, boolean likedByMe) {
        return ProductListResponse.builder()
                .id(product.getId())
                .thumbnail(product.getThumbnail())
                .category(product.getCategory())
                .manufacturerName(product.getManufacturer())
                .productName(product.getName())
                .discountedPrice(getPrice(product))
                .likedByMe(likedByMe)
                .build();
    }
}

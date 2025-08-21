package site.kikihi.custom.platform.adapter.in.web.dto.response.product;

import site.kikihi.custom.platform.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.*;

/**
 * 상품 상세 응답 DTO
 *
 * @param id                 상품 ID
 * @param thumbnail          상품 썸네일
 * @param manufacturerName   제조사명
 * @param category           카테고리
 * @param productName        제품명
 * @param originalPrice      최저가
 * @param likedByMe          나의 북마크 여부
 * @param cautions           상품 유의사항
 * @param imageUrl           상세 정보 이미지 URL 목록
 */

@Builder
@Schema(
        name = "[응답][상품] 상품 상세 조회 Response",
        description = "상품 상세 정보를 반환하는 응답 DTO입니다."
)
public record ProductDetailResponse(
        @Schema(description = "상품 아이디", example = "101")
        String id,

        @Schema(description = "상품 썸네일 이미지 URL", example = "https://example.com/product/101.jpg")
        String thumbnail,

        @Schema(description = "제조사명", example = "독거미")
        String manufacturerName,

        @Schema(description = "카테고리명", example = "keyboard")
        String category,

        @Schema(description = "제품명", example = "독거미 Aula F99")
        String productName,

        @Schema(description = "최저가 가격부터", example = "599000 ~")
        String originalPrice,

        @Schema(description = "북마크(좋아요)한 상품 여부", example = "true")
        boolean likedByMe,

        @Schema(description = "상품 유의사항", example = "도착일은 배송지나 배송사 사정으로 변경 또는 지연될 수 있습니다.")
        String cautions,

        @Schema(description = "상품 옵션")
        List<ProductOptions> options,

        @Schema(description = "상세 정보 이미지 URL 목록", example = "[\"https://example.com/img1.jpg\", \"https://example.com/img2.jpg\"]")
        List<String> imageUrl
) {

    /// 정적 팩토리 메서드
    public static ProductDetailResponse from(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .thumbnail(product.getThumbnail())
                .manufacturerName(product.getManufacturer())
                .category(product.getCategory())
                .productName(product.getName())
                .originalPrice(getPrice(product))
                .likedByMe(false)
                .options(ProductOptions.from(product))
                .cautions("도착일은 배송지나 배송사 사정으로 변경 또는 지연될 수 있습니다.")
                .imageUrl(product.getAllDetailImages())
                .build();
    }

    /// 정적 팩토리 메서드
    public static ProductDetailResponse from(Product product, boolean likedByMe) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .thumbnail(product.getThumbnail())
                .manufacturerName(product.getManufacturer())
                .category(product.getCategory())
                .productName(product.getName())
                .originalPrice(getPrice(product))
                .likedByMe(likedByMe)
                .options(ProductOptions.from(product))
                .cautions("도착일은 배송지나 배송사 사정으로 변경 또는 지연될 수 있습니다.")
                .imageUrl(product.getAllDetailImages())
                .build();
    }


    /// 최저가 가격을 위한 설정
    private static String getPrice(Product product) {

        // 옵션 Optional 처리
        List<Map<String, Object>> productOptions = Optional.ofNullable(product.getOptions())
                .orElse(Collections.emptyList());

        // 옵션이 없다면 기본 가격 제공
        if (productOptions.isEmpty()) {
            return (int) product.getPrice() + "원";
        }

        // 옵션 중 최저가 찾기
        OptionalDouble minPrice = productOptions.stream()
                .map(option -> option.get("main_price"))
                .filter(Objects::nonNull)
                .mapToDouble(price -> Double.parseDouble(price.toString()))
                .min();

        // 최저가 있으면 옵션 가격으로, 없으면 기본 가격으로
        if (minPrice.isPresent()) {
            return (int) minPrice.getAsDouble() + "원 ~";
        } else {
            return (int) product.getPrice() + "원";
        }
    }


    /// 내부에서만 사용되는 옵션
    @Builder
    private record ProductOptions(
            String optionName,
            Double price,
            String site,
            String siteUrl
    ) {

        /// 정적 팩토리 메서드
        private static ProductOptions of(String optionName, Double price, String site, String siteUrl) {
            return ProductOptions.builder()
                    .optionName(optionName)
                    .price(price)
                    .site(site)
                    .siteUrl(siteUrl)
                    .build();
        }

        /// 정적 팩토리 메서드
        public static List<ProductOptions> from(Product product) {
            List<Map<String, Object>> productOptions = Optional.ofNullable(product.getOptions())
                    .orElse(Collections.emptyList());

            return productOptions.stream()
                    /// option_name 없는 건 필터링
                    .filter(option -> option.get("option_name") != null)
                    .map(option -> {

                        /// 옵션 명 가져오기
                        String optionName = (String) option.get("option_name");
                        Double price = null;

                        /// 가격이 있다면
                        if (option.get("main_price") != null) {
                            price = Double.valueOf(option.get("main_price").toString());
                        }

                        String site = null;
                        String siteUrl = null;
                        Object vendorsObj = option.get("vendors");
                        if (vendorsObj instanceof Map<?, ?> vendors) {
                            site = (String) vendors.get("shop");
                            siteUrl = (String) vendors.get("url");
                        }

                        return of(optionName, price, site, siteUrl);
                    })
                    .toList();
        }
    }


}


package site.kikihi.custom.platform.adapter.in.web.dto.response.util;

import org.springframework.stereotype.Component;
import site.kikihi.custom.platform.domain.product.Product;

import java.util.*;

@Component
public class ProductPriceUtil {

    /// 최저가 가격을 위한 설정
    public static String getPrice(Product product) {

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

}

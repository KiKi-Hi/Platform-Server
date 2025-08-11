package com.jiyoung.kikihi.platform.adapter.in.web.converter;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.product.CategoryType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * 파라미터 변환을 위한 컨버터입니다.
 */
@Component
public class CategoryTypeConverter implements Converter<String, CategoryType> {

    /**
     * 문자열을 CategoryType enum으로 변환합니다. 대소문자 구분 없이 비교합니다.
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     */
    @Override
    public CategoryType convert(String source) {
        for (CategoryType type : CategoryType.values()) {
            if (type.getValue().equalsIgnoreCase(source)) {
                return type;
            }
        }
        throw new IllegalArgumentException(ErrorCode.INVALID_INPUT.getMessage());
    }
}

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
     * getCode를 바탕으로 4DX를 인식하고, 대소문자 관계없이 비교해서 바꾸는 형식입니다.
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

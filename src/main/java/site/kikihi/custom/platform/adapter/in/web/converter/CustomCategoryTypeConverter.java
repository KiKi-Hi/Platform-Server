package site.kikihi.custom.platform.adapter.in.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import site.kikihi.custom.global.response.ErrorCode;
import site.kikihi.custom.platform.adapter.in.web.dto.request.custom.CustomCategoryType;

/**
 * 파라미터 변환을 위한 컨버터입니다.
 */
@Component
public class CustomCategoryTypeConverter implements Converter<String, CustomCategoryType> {

    /**
     * 문자열을 CategoryType enum으로 변환합니다. 대소문자 구분 없이 비교합니다.
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     */
    @Override
    public CustomCategoryType convert(String source) {
        for (CustomCategoryType type : CustomCategoryType.values()) {
            if (type.getValue().equalsIgnoreCase(source)) {
                return type;
            }
        }
        throw new IllegalArgumentException(ErrorCode.INVALID_INPUT.getMessage());
    }
}

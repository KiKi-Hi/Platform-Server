package com.kikihi.store.platform.adapter.in.web.dto.response.custom;

import com.kikihi.store.platform.domain.custom.CustomKeyboardLayout;
import lombok.Builder;
import java.util.List;

/**
 *
 * @param typeName
 * @param description
 */

@Builder
public record CustomKeyboardLayoutResponse(
        String typeName,
        String description
) {

    /// 정적 팩토리 메서드
    public static CustomKeyboardLayoutResponse from(CustomKeyboardLayout layout) {
        return CustomKeyboardLayoutResponse.builder()
                .typeName(layout.getLayoutName())
                .description(layout.getDescription())
                .build();
    }

    /// 정적 팩토리 메서드
    public static List<CustomKeyboardLayoutResponse> from(List<CustomKeyboardLayout> layouts) {
        return layouts.stream()
                .map(CustomKeyboardLayoutResponse::from)
                .toList();
    }
}

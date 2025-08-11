package com.jiyoung.kikihi.platform.adapter.in.web.dto.request.product;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
    KEYBOARD("keyboard"),
    HOUSING("housing"),
    SWITCH("switch"),
    KEYCAP("keycap"),
    ACCESSORY("accessory"),
    CASE("case");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}

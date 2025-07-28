package com.jiyoung.kikihi.platform.domain.custom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
@Getter
public enum CustomKeyboardLayout {
    PERCENT_60("60배열"),
    PERCENT_75("75배열"),
    TENKEYLESS("텐키리스"),
    FULLSIZE("풀배열");

    private final String name;

    /// 리스트 목록 조회
    public static List<CustomKeyboardLayout> getKeyboardLayouts() {
        return Arrays.asList(CustomKeyboardLayout.values());
    }

    /// 이름 조회
    @JsonIgnore
    public String getLayoutName() {
        return name;
    }

}


package com.kikihi.store.platform.domain.custom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public enum CustomKeyboardLayout {
    PERCENT_60("60배열","미니멀의 정점"),
    PERCENT_75("75배열","컴팩트한 사이즈의 깔끔함"),
    TENKEYLESS("텐키리스","넘버패드없이 깔끔한 키보드"),
    FULLSIZE("풀배열","원조의 품격");

    private final String name;
    private final String description;

    /// 리스트 목록 조회
    public static List<CustomKeyboardLayout> getKeyboardLayouts() {
        return Arrays.asList(CustomKeyboardLayout.values());
    }

    /// 이름 조회
    @JsonIgnore
    public String getLayoutName() {
        return name;
    }

    @JsonIgnore
    public String getDescription() {
        return description;
    }

}


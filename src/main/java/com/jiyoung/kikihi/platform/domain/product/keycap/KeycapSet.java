package com.jiyoung.kikihi.platform.domain.product.keycap;

import lombok.Getter;

@Getter
public enum KeycapSet {
    FULL_SET("풀세트"),
    TENKEYLESS("텐키리스"),
    MINI_SET("미니 세트");

    private final String displayName;

    KeycapSet(String displayName) {
        this.displayName = displayName;
    }
}
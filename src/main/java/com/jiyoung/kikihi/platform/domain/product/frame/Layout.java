package com.jiyoung.kikihi.platform.domain.product.frame;

import lombok.Getter;

@Getter
public enum Layout {
    SIXTY_PERCENT("60%"),
    SEVENTY_PERCENT("70%"),
    TKL("TKL"),
    FULL_SIZE("풀사이즈"),
    TENKEYLESS("텐키리스");

    private final String displayName;

    Layout(String displayName) {
        this.displayName = displayName;
    }

}
package com.jiyoung.kikihi.platform.domain.product.switches;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SwitchType {
    CLICKY("Clicky"),
    TACTILE("Tactile"),
    LINEAR("Linear"),
    SILENT("Silent"),
    SPECIAL("Special");

    private final String typeName;
}
package com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter;

import com.jiyoung.kikihi.platform.domain.product.keycap.KeycapMaterial;
import com.jiyoung.kikihi.platform.domain.product.keycap.KeycapSet;
import com.jiyoung.kikihi.platform.domain.product.keycap.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeycapFilter implements BaseFilter {
    private KeycapMaterial material; // 키캡 재질 (예: ABS, PBT, POM, PC 등)
    private Profile profile;
    private String printMethod;
    private KeycapSet kepcapSet;
}

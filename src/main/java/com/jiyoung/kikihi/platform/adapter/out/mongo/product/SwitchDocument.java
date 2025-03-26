package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import com.jiyoung.kikihi.platform.domain.product.keycap.KeycapMaterial;
import com.jiyoung.kikihi.platform.domain.product.keycap.KeycapSet;
import com.jiyoung.kikihi.platform.domain.product.keycap.Profile;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "switch")
@SuperBuilder
public class SwitchDocument {
    private KeycapMaterial material; // 키캡 재질 (예: ABS, PBT, POM, PC 등)
    private Profile profile;
    private String printMethod;
    private KeycapSet kepcapSet;
}

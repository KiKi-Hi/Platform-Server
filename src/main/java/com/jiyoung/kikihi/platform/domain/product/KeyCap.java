package com.jiyoung.kikihi.platform.domain.product;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "keyboards") // MongoDB 컬렉션 이름
public class KeyCap extends BaseProduct{
}

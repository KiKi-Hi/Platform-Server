package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "keyCap") // MongoDB 컬렉션 이름
public class KeyCapDocument extends BaseProductDocument {
}

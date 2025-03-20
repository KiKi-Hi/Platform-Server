package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "keyboard") // MongoDB 컬렉션 이름
public class KeyBoardDocument extends BaseProductDocument {
}

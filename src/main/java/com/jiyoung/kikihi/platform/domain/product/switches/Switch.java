package com.jiyoung.kikihi.platform.domain.product.switches;

import com.jiyoung.kikihi.platform.domain.product.Product;
import org.springframework.data.mongodb.core.mapping.Document;

@Document()
public class Switch extends Product {
     private SwitchType switchType; // 스위치 유형 (예: Clicky, Tactile, Linear 등)
     private int actuationForce; // 작동 압력 (스위치 눌림 강도, 단위는 gf 등)
     private double totalTravel; // 총 이동 거리 (스위치의 눌림 깊이)
     private double springWeight; // 스프링 강도 (스프링의 압력 강도)
     private boolean lubrication; // 윤활 여부 (스위치에 윤활 처리가 되었는지)
     private int switchCount; // 스위치 개수 (총 키 개수)
}

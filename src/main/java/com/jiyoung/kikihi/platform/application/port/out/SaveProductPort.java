package com.jiyoung.kikihi.platform.application.port.out;


import java.util.Optional;

public interface SaveProductPort {

    // 상품 등록하기
//    Product createProduct(Product product);

    // 상품 재고 조회
    Optional<Integer> findStockByProductId(Long productId);

    // 상품 재고 -1하기 -> 서비스에서 엮은 것이 좋음 (사용자 요청과는 관련없음-결제되면 깎자)
    boolean decreaseProduct(Long productId, Integer quantity);
}

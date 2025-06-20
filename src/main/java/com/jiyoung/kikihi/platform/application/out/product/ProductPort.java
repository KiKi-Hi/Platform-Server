package com.jiyoung.kikihi.platform.application.out.product;

import com.jiyoung.kikihi.platform.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

/**
 * 상품 관련 Port 입니다.
 * DB 내부 CRUD 담당하는 인터페이스입니다.
 */

public interface ProductPort {

    /// 저장
    Product saveProduct(Product product);

    /// 조회
    Product getProduct(String productId);

    // 상품 전체 조회
    List<Product> getProducts();

    // 페이징 처리
    Page<Product> getProducts(Pageable pageable);

    /// 삭제
    void deleteProduct(String productId);

}

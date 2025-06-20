package com.jiyoung.kikihi.platform.application;

import com.jiyoung.kikihi.platform.application.in.product.ProductUseCase;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductPort productPort;

    @Override
    public Page<Product> search(String keyword, String category) {
        return null;
    }

    @Override
    public List<Product> getProducts() {
        return productPort.getProducts();
    }

    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return productPort.getProducts(pageable);
    }

    @Override
    public Page<Product> getProductsByCategoryId(Pageable pageable, Long categoryId) {
        return null;
    }

    @Override
    public Product getProduct(String id) {
        return null;
    }
}

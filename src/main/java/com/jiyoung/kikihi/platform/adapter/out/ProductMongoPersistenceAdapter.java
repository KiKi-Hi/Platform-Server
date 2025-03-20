package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.mongo.product.ProductDocumentMongoRepository;
import com.jiyoung.kikihi.platform.application.port.out.product.DeleteProductPort;
import com.jiyoung.kikihi.platform.application.port.out.product.LoadProductPort;
import com.jiyoung.kikihi.platform.application.port.out.product.SaveProductPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductMongoPersistenceAdapter implements SaveProductPort, LoadProductPort, DeleteProductPort {

    private final ProductDocumentMongoRepository repository;

    @Override
    public void deleteProduct(Long productId) {

    }

    @Override
    public Optional<Product> loadProductById(Long productId) {
        return Optional.empty();
    }

    @Override
    public Page<Product> loadProducts(Pageable pageable, Long categoryId) {
        return null;
    }

    @Override
    public Page<Product> loadProductsByLike(Pageable pageable, Long categoryId) {
        return null;
    }

    @Override
    public Page<Product> loadProductsByCondition(Pageable pageable, Long categoryId, String productTitle, Double minPrice, Double maxPrice) {
        return null;
    }

    @Override
    public Integer findStockByProductId(Long productId) {
        return 0;
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public boolean decreaseProduct(Long productId, Integer quantity) {
        return false;
    }
}

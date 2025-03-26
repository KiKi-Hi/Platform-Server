package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.FrameFilter;
import com.jiyoung.kikihi.platform.adapter.out.mongo.product.FrameDocument;
import com.jiyoung.kikihi.platform.adapter.out.mongo.product.FrameMongoRepository;
import com.jiyoung.kikihi.platform.application.port.out.product.DeleteProductPort;
import com.jiyoung.kikihi.platform.application.port.out.product.LoadProductPort;
import com.jiyoung.kikihi.platform.application.port.out.product.SaveProductPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import com.jiyoung.kikihi.platform.domain.product.frame.Frame;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductMongoPersistenceAdapter implements SaveProductPort, LoadProductPort, DeleteProductPort {

    private final FrameMongoRepository frameMongoRepository;

    @Override
    public Page<Frame> getFrames(FrameFilter filter, Pageable pageable) {
        Page<FrameDocument> frameDocuments =frameMongoRepository.findAllBy(pageable);

        return frameDocuments.map(FrameDocument::toDomain);
    }


    @Override
    public Page<Frame> findAllFrames(Pageable pageable) {
        Page<FrameDocument> frameDocuments =frameMongoRepository.findAllBy(pageable);
        return frameDocuments.map(FrameDocument::toDomain);
    }




    @Override
    public void deleteProduct(Long productId) {

    }



    @Override
    public Optional<Product> loadProductById(Long productId) {
        return Optional.empty();
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

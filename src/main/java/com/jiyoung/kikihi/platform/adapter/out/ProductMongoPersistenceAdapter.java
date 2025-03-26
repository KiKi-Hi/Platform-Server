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
        System.out.println("Fetching frames...");
        Page<FrameDocument> frameDocuments =frameMongoRepository.findAll(pageable);
        System.out.println("Fetched " + frameDocuments.getTotalElements() + " frames.");

        System.out.println(frameDocuments.getContent());
        return frameDocuments.map(FrameDocument::toDomain);
//        Page<FrameDocument> frameDocuments = frameMongoRepository.findByFilter(
//                filter.getMaterial(),
//                filter.getMountType(),
//                filter.getSoundDampening(),
//                filter.getWeight(),
//                filter.getLayout(),
//                pageable);
//
//        System.out.println("FrameDocuments fetched: " + frameDocuments.getContent());
//
//        // frameDocuments 안의 각 FrameDocument 객체를 Frame 객체로 변환
//        return frameDocuments.map(FrameDocument::toDomain);
    }

    @Override
    public Page<Frame> findAllFrames(Pageable pageable) {
        return frameMongoRepository.findAllBy(pageable)
                .map(FrameDocument::toDomain);
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

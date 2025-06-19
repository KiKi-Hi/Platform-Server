package com.jiyoung.kikihi.platform.application;

import com.jiyoung.kikihi.platform.application.in.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

}

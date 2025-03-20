package com.jiyoung.kikihi.platform.application.port.in.product;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.OrderRequest;
import com.jiyoung.kikihi.platform.domain.order.Order;

public interface CreateOrderCustomUseCase {

    // 주문을 생성한다.
    Order create(OrderRequest request);

}

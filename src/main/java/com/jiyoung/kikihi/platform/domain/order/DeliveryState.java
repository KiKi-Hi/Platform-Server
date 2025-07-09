package com.jiyoung.kikihi.platform.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryState {
    ORDER_RECEIVED("Order Received"),
    PAYMENT_CONFIRMED("Payment Confirmed"),
    PREPARING("Preparing"),
    PACKING("Packing"),
    SHIPPING("In Transit"),
    DELIVERED("Delivered"),
    DELIVERY_ON_HOLD("Delivery On Hold"),
    RETURN_REQUESTED("Return Requested"),
    RETURN_COMPLETED("Return Completed"),
    CANCELLATION_REQUESTED("Cancellation Requested"),
    ORDER_CANCELLED("Order Cancelled");

    private final String description;
}

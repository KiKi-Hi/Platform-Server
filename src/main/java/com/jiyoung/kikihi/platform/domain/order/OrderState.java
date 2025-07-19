package com.jiyoung.kikihi.platform.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderState {

    READY("결제 준비"),
    IN_PROGRESS("결제 승인 중"),
    WAITING_FOR_DEPOSIT("입금 대기"),
    DONE("결제 성공"),
    CANCELED("결제 취소"),
    PARTIAL_CANCELED("결제 부분 취소"),
    ABORTED("결제 실패"),
    EXPIRED("결제 만료");

    private final String description;

    /**
     * Toss status 값을 내부 OrderState enum으로 변환
     */
    public static OrderState fromTossStatus(String status) {
        try {
            return OrderState.valueOf(status); // Toss의 status와 enum name이 정확히 일치
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Toss 응답 status에 매핑되지 않는 값: " + status);
        }
    }
}

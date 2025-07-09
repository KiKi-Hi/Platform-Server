package com.jiyoung.kikihi.global.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.lang.Nullable;
import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        @JsonIgnore
        HttpStatus httpStatus,
        boolean success,
        @Nullable T data,
        @Nullable ExceptionDto error
) {

    // 200 OK 응답
    public static <T> ApiResponse<T> ok(@Nullable final T data) {
        return new ApiResponse<>(HttpStatus.OK, true, data, null);
    }

    // 201 Created 응답
    public static <T> ApiResponse<T> created(@Nullable final T data) {
        return new ApiResponse<>(HttpStatus.CREATED, true, data, null);
    }

    // 에러 처리
    public static <T> ApiResponse<T> fail(final CustomException e) {
        return new ApiResponse<>(
                e.getErrorCode().getHttpStatus(), false, null, ExceptionDto.of(e.getErrorCode(), e.getMessage()) // 예외 메시지 추가
        );
    }

}

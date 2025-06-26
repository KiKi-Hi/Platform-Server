package com.jiyoung.kikihi.global.exception;


import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.CustomException;
import com.jiyoung.kikihi.global.response.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
//@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("[예외 발생] EntityNotFoundException: {}", ex.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.NOT_FOUND_END_POINT,ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleIllegalStateException(Exception ex) {
        log.error("[예외 발생] Exception: {}", ex.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST,ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("[권한 없음] Exception: {}", ex.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.FORBIDDEN,ex.getMessage()));
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleGeneralException(Exception ex) {
        log.error("[예외 발생] Exception: {}", ex.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR,ex.getMessage()));
    }
//
//    @ExceptionHandler(NotVerifiedException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiResponse<?> handleNotVerifiedException(NotVerifiedException ex) {
//        log.error("[예외 발생] Exception: {}", ex.getMessage());
//        return ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST,ex.getMessage()));
//    }
}

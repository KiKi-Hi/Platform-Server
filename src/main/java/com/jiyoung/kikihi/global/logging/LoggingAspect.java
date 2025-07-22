package com.jiyoung.kikihi.global.logging;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.jiyoung.kikihi.platform.application.service.*Service.*(..))")
    private void applicationLayer() {
    }

    @Before("applicationLayer()")
    public void logMethodEntry(JoinPoint joinPoint) {
        log.info("[서비스 로깅] 메서드 진입: {}.{}({})",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "applicationLayer()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        log.info("[서비스 로깅] 메서드 종료: {}.{} => 반환: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(pointcut = "applicationLayer()")
    public void logException(JoinPoint joinPoint) {
        log.info("[서비스 로깅] 예외 발생: {}.{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }
}



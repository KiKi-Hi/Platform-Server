package site.kikihi.custom.global.logging;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* site.kikihi.custom.platform.application.service.*Service.*(..))")
    private void applicationLayer() {
    }

    @Around("applicationLayer()")
    public Object logProcessTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();  // 실제 메서드 실행

        long executionTime = System.currentTimeMillis() - start;

        log.info("[서비스 로깅] 메서드 실행 시간: {}.{} 실행 시간 = {}ms",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                executionTime);

        return proceed;
    }

    @AfterThrowing(pointcut = "applicationLayer()")
    public void logException(JoinPoint joinPoint) {
        log.info("[서비스 로깅] 예외 발생: {},{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }
}



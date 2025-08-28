package site.kikihi.custom.global.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

    @Pointcut("execution(* site.kikihi.custom.platform.adapter.in.web..*.*(..))")
    private void controllerPointcut() {}

    @Before("controllerPointcut()")
    public void logBeforeExecution(JoinPoint joinPoint) throws Throwable {

        // Request 정보
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "None";

        // 호출된 컨트롤러와 메서드 정보
        String controllerName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Map<String, Object> params = new HashMap<>();


        // URI 인코딩 및 출력에 필요한 변수들
        try {
            String decodedRequestURI = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
            params.put("controller", controllerName);
            params.put("method", methodName);
            params.put("http_method", request.getMethod());
            params.put("request_uri", decodedRequestURI);
            params.put("user", username);
        } catch (Exception e) {
            log.error("LogAspect Error", e);
        }

        // 로그 출력
        log.info("요청 정보 - 전체URL: {} | URI: {} | 메서드: {} | 유저: {} | Origin: {}",
                request.getRequestURL(),
                request.getRequestURI(),
                request.getMethod(),
                username,
                request.getHeader("Origin"));

    }
}

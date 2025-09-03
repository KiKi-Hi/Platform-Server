package site.kikihi.custom.global.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
/**
 * HTTP 요청에 대한 로그를 기록한다.
 */
@Order(Integer.MIN_VALUE)
public class LogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /// 요청 로그 남기기
        String ipAddress = request.getRemoteAddr();
        String httpMethod = request.getMethod();
        String uri = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";

        /// 로그스태시로 넘길 로깅 출력
        log.info("[HTTP 로깅]: {}, [{}], {}, {}", ipAddress, httpMethod, uri, username);

        /// 로그 남기고 넘기기
        filterChain.doFilter(request, response);

    }
}

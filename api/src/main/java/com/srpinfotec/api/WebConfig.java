package com.srpinfotec.api;

import com.srpinfotec.api.util.HttpUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .maxAge(60*60*24) // 24시간동안 preflight caching
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedOrigins(
                        "https://srp.srpinfotec.com",
                        "https://srp.srpinfotec.com:8082",
                        "https://sr.srpinfotec.com:18082",
                        "http://localhost:8106"
                );
    }

    @Bean
    @Order(1)
    public Filter accessLoggingFilter(){
        return new Filter() {
            private static final List<String> whiteList = Arrays.asList("/", "/actuator/prometheus");

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
                Filter.super.init(filterConfig);
            }

            @Override
            public void destroy() {
                Filter.super.destroy();
            }

            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
                long startTime = System.currentTimeMillis();    // 시작 시간

                // Request, Response 래핑
                HttpServletRequest request = (HttpServletRequest) servletRequest;
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

                // Request Id 등록
                String requestId = HttpUtil.getRequestId();

                MDC.put("request_id", requestId);

                // Request Logging
                String uri = request.getRequestURI();
                String ip = HttpUtil.getRequestIp();
                String method = request.getMethod();
                String userId = request.getHeader("X-SRP-User-Id");

                chain.doFilter(requestWrapper, response);

                int status = response.getStatus();
                long elapsedTime = System.currentTimeMillis() - startTime;

                if(!whiteList.contains(uri) && !method.equalsIgnoreCase("OPTIONS")){
                    log.info("[{}] [{}] [{}] [{}] [{}] - [{}] {}ms", userId, requestId, ip, method, uri, status, elapsedTime);
                }

                MDC.clear();
            }
        };
    }
}

package com.srpinfotec.batch.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Slf4j
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .maxAge(60*60*24) // 24시간동안 preflight caching
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedOrigins(
                        "https://srp.srpinfotec.com",
                        "https://srp.srpinfotec.com:8082",
                        "https://sr.srpinfotec.com:18082"
                );
    }
}

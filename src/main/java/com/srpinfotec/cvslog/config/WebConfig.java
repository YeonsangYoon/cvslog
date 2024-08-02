package com.srpinfotec.cvslog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedOrigins("http://localhost:8106", "https://srp.srpinfotec.com", "https://srp.srpinfotec.com:8082");
    }
}

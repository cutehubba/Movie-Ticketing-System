package com.movie.ticketing.userservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsGlobalConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")          // 允许所有路径
                .allowedOrigins("http://localhost:5173")  // 允许的前端地址
                .allowedMethods("*")        // 允许所有方法（GET/POST等）
                .allowedHeaders("*")        // 允许所有请求头
                .allowCredentials(true)     // 允许凭证（如cookies）
                .maxAge(3600);             // 预检请求缓存时间（秒）
    }
}
package com.lei.mes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Web 配置类 ，跨域配置
 * @author lei
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private static final long MAX_AGE = 3600L; // 1小时
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有路径应用跨域策略
                .allowedOrigins("http://localhost:5173", "https://test.com") // 明确来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*") // 允许的头部信息，可以使用*表示任意头部信息
                .allowCredentials(true) // 是否允许发送Cookie信息，这对于前端JavaScript的跨域请求很重要，但要确保前端设置了withCredentials为true。
                .maxAge(MAX_AGE); // 预检请求的缓存时间（秒）
    }
}
package com.lei.mes.config;

import com.lei.mes.interceptor.JwtAuthenticationInterceptor;
import com.lei.mes.util.JwtUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * @author lei
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtUtils jwtUtils;

    // 构造函数注入
    public WebMvcConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthenticationInterceptor(jwtUtils))
                .addPathPatterns("/**")           // 拦截所有路径
                //todo 暂时排除/api/sys 下所有系统接口路径
                .excludePathPatterns("/auth/login", "/auth/register", "/api/sys/**"); // 排除登录、注册等
    }
}
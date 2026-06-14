package com.lei.mes.config;

import com.lei.mes.interceptor.JwtAuthenticationInterceptor;
import com.lei.mes.service.user.SysUserService;
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
    private final SysUserService sysUserService;

    // 构造函数注入
    public WebMvcConfig(JwtUtils jwtUtils, SysUserService sysUserService) {
        this.jwtUtils = jwtUtils;
        this.sysUserService = sysUserService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthenticationInterceptor(jwtUtils, sysUserService))
                .addPathPatterns("/**")           // 拦截所有路径
                .excludePathPatterns("/api/sys/user/login", "/api/sys/sys/user/register", "/public/**"); // 排除登录、注册等
    }
}
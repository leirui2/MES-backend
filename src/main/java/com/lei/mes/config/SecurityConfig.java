package com.lei.mes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 * 开发阶段：放行所有接口，关闭 CSRF（前后端分离）
 * 生产环境：需要放开注释，开启具体接口鉴权
 * @author lei
 */
@Configuration
public class SecurityConfig {

    // 密码加密器注册为 Bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 安全过滤链注册为 Bean
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 关闭 CSRF（前后端分离不需要 CSRF Token）
            .csrf().disable()
            // 放行所有接口（开发阶段）
            .authorizeHttpRequests()
                .requestMatchers("/**").permitAll()
            .and()
            // 关闭 Http 头部缓存（避免浏览器缓存登录页）
            .headers().cacheControl();
        return http.build();
    }
}

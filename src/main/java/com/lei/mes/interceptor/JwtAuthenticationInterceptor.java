package com.lei.mes.interceptor;

import com.lei.mes.entity.user.SysUser;
import com.lei.mes.service.user.SysUserService;
import com.lei.mes.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器
 * @author lei
 */
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private final SysUserService sysUserService;

    public JwtAuthenticationInterceptor(JwtUtils jwtUtils, SysUserService sysUserService) {
        this.jwtUtils = jwtUtils;
        this.sysUserService = sysUserService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 如果是预检请求（OPTIONS），直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 1. 获取并验证 Token
        String token = resolveToken(request);
        if (token == null || !jwtUtils.validateToken(token).valid()) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或Token无效\"}");
            return false;
        }

        // 2. 提取用户信息（可考虑缓存，避免每次查库）
        Long userId = jwtUtils.extractUserId(token);
        SysUser user = sysUserService.getById(userId);
        if (user == null || user.getStatus() == 0) {
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"用户不存在或已禁用\"}");
            return false;
        }

        // 3. 将用户信息存入 request attribute，供后续 Controller/Service 使用
        request.setAttribute("currentUser", user);
        return true; // 放行
    }

    private String resolveToken(HttpServletRequest request) {
        // 从请求头中获取 Authorization: Bearer xxx 格式的 Token
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
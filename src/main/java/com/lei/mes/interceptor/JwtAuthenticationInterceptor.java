package com.lei.mes.interceptor;

import com.lei.mes.util.JwtUtils;
import com.lei.mes.util.UserContext;
import com.lei.mes.util.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器
 * @author lei
 */
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationInterceptor(JwtUtils jwtUtils ) {
        this.jwtUtils = jwtUtils;
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

        // 2. 从token中提取用户信息
        Long userId = jwtUtils.extractUserId(token);
        String userName = jwtUtils.extractUsername(token);

        // 3. 将用户信息存入 ThreadLocal，供后续 Controller/Service 使用
        UserContext userContext = new UserContext();
        userContext.setUserId(userId);
        userContext.setUserName(userName);
        //存入
        UserContextHolder.setUserContext(userContext);
        return true; // 放行
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        // 清除 ThreadLocal 中的用户信息
        UserContextHolder.remove();
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
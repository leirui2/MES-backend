package com.lei.mes.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT 工具类
 * - 生成访问 Token
 * - 生成刷新 Token
 * - 从 Token 提取用户名、用户 ID
 * - 验证 Token 是否有效
 * @author lei
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成访问 Token（短效，用于接口鉴权）
     */
    public String generateAccessToken(Long userId, String username) {
        return buildToken(userId, username, accessExpiration);
    }

    /**
     * 生成刷新 Token（长效，用于续期）
     */
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return Jwts.builder()
                .claims(claims)         // 自定义字段存放刷新类型
                .subject(username)      // 主题，一般存用户名
                .issuedAt(new Date())   // 发布时间
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration)) // 过期时间
                .signWith(getSigningKey())
                .compact(); // 生成 Token
    }

    /**
     * 构建 Token（内部方法）
     */
    private String buildToken(Long userId, String username, long expirationMs) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)      // 主题，一般存用户名
                .claim("userId", userId) // 自定义字段存放用户ID
                .issuedAt(now)          // 发布时间
                .expiration(new Date(now.getTime() + expirationMs)) // 过期时间
                .signWith(getSigningKey()) // 签名密钥
                .compact(); // 生成 Token
    }

    /**
     * 从 Token 中提取用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从 Token 中提取用户 ID
     */
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * 从 Token 中提取过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 从 Token 中提取指定类型的 Claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 解析 Token（内部统一方法）
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 验证 Token 是否有效（未过期 + 用户名匹配）
     */
    public boolean isTokenValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    /**
     * 判断 Token 是否过期
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 验证 Token 格式，返回具体错误类型
     */
    public TokenValidationResult validateToken(String token) {
        try {
            parseClaims(token);
            return new TokenValidationResult(true, "有效", null);
        } catch (ExpiredJwtException e) {
            return new TokenValidationResult(false, "Token已过期", "EXPIRED");
        } catch (MalformedJwtException e) {
            return new TokenValidationResult(false, "Token格式错误", "MALFORMED");
        } catch (SignatureException e) {
            return new TokenValidationResult(false, "签名验证失败", "INVALID_SIGNATURE");
        } catch (UnsupportedJwtException e) {
            return new TokenValidationResult(false, "不支持的Token类型", "UNSUPPORTED");
        } catch (IllegalArgumentException e) {
            return new TokenValidationResult(false, "Token为空", "EMPTY");
        }
    }

    /**
     * Token 验证结果
     */
    public record TokenValidationResult(
            boolean valid,
            String message,
            String errorType
    ) {}
}

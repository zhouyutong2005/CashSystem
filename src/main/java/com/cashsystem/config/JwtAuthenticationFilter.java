package com.cashsystem.config;

import com.cashsystem.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    // 黑名单 key 前缀（退出登录时写入）
    private static final String BLACKLIST_PREFIX = "token:blacklist:";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // 放行登录和测试接口
        if (path.startsWith("/api/auth/login") ||
                path.startsWith("/api/test") ||
                path.startsWith("/api/auth/test")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 获取token
        String token = getTokenFromRequest(request);

        if (token != null && jwtUtil.validateToken(token)) {
            try {
                // 检查 token 是否在黑名单中（用户已退出登录）
                if (Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token))) {
                    log.warn("Token 已在黑名单中（用户已退出登录）");
                    SecurityContextHolder.clearContext();
                    filterChain.doFilter(request, response);
                    return;
                }

                // 从token中获取用户信息
                String username = jwtUtil.getUsernameFromToken(token);
                Long userId = jwtUtil.getUserIdFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);

                // 创建认证信息
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                        );

                // 将用户ID存储到请求属性中，方便后续使用
                request.setAttribute("userId", userId);
                request.setAttribute("userRole", role);

                // 设置认证信息到SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("JWT认证成功: username={}, role={}", username, role);

            } catch (Exception e) {
                log.error("JWT认证失败: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        } else {
            log.warn("JWT token无效或不存在");
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
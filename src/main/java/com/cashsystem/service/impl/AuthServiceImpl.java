package com.cashsystem.service.impl;

import com.cashsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisTemplate<String, Object> redisTemplate;

    // Redis key 前缀，避免和其他 key 冲突
    private static final String TOKEN_PREFIX = "token:user:";
    // Token 有效期，和 JWT 保持一致（24小时）
    private static final long TOKEN_EXPIRE_HOURS = 24;

    /**
     * 将 token 和用户信息存入 Redis
     * 供 UserService 登录成功后调用
     */
    public void storeToken(String token, Long userId, String username, String role) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("username", username);
        userInfo.put("role", role);

        // 存入 Redis Hash，key = "token:user:{token}"，过期时间 24 小时
        String redisKey = TOKEN_PREFIX + token;
        redisTemplate.opsForHash().putAll(redisKey, userInfo);
        redisTemplate.expire(redisKey, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);

        log.debug("Token 已存入 Redis: userId={}, username={}", userId, username);
    }

    /**
     * 退出登录：从 Redis 删除 token（主动失效）
     */
    public void removeToken(String token) {
        redisTemplate.delete(TOKEN_PREFIX + token);
        log.info("Token 已从 Redis 删除（用户退出登录）");
    }

    @Override
    public boolean validateToken(String token) {
        try {
            // 检查 Redis 中是否存在该 token
            return Boolean.TRUE.equals(redisTemplate.hasKey(TOKEN_PREFIX + token));
        } catch (Exception e) {
            log.error("Token 验证失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Long getUserIdFromToken(String token) {
        try {
            Object value = redisTemplate.opsForHash().get(TOKEN_PREFIX + token, "userId");
            if (value == null) return null;
            // Redis 反序列化后可能是 Integer，需要转成 Long
            return value instanceof Integer ? ((Integer) value).longValue() : (Long) value;
        } catch (Exception e) {
            log.error("从 Redis Token 获取用户ID失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            Object value = redisTemplate.opsForHash().get(TOKEN_PREFIX + token, "username");
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            log.error("从 Redis Token 获取用户名失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String getRoleFromToken(String token) {
        try {
            Object value = redisTemplate.opsForHash().get(TOKEN_PREFIX + token, "role");
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            log.error("从 Redis Token 获取角色失败: {}", e.getMessage());
            return null;
        }
    }
}

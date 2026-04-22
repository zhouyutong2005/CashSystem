package com.cashsystem.service.impl;

import com.cashsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // 临时存储token和用户信息的映射（生产环境应该用Redis）
    private final Map<String, Map<String, Object>> tokenStore = new HashMap<>();

    @Override
    public boolean validateToken(String token) {
        try {
            // 简化验证：检查token是否在存储中
            return tokenStore.containsKey(token);
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Long getUserIdFromToken(String token) {
        try {
            Map<String, Object> userInfo = tokenStore.get(token);
            return userInfo != null ? (Long) userInfo.get("userId") : null;
        } catch (Exception e) {
            log.error("从Token获取用户ID失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            Map<String, Object> userInfo = tokenStore.get(token);
            return userInfo != null ? (String) userInfo.get("username") : null;
        } catch (Exception e) {
            log.error("从Token获取用户名失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String getRoleFromToken(String token) {
        try {
            Map<String, Object> userInfo = tokenStore.get(token);
            return userInfo != null ? (String) userInfo.get("role") : null;
        } catch (Exception e) {
            log.error("从Token获取用户角色失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 生成简化版token（供UserService使用）
     */
    public String generateSimpleToken(Long userId, String username, String role) {
        String token = "token_" + System.currentTimeMillis() + "_" + userId;
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("username", username);
        userInfo.put("role", role);
        tokenStore.put(token, userInfo);
        return token;
    }

    /**
     * 清除过期的token（可选）
     */
    public void cleanupExpiredTokens() {
        // 简化处理：可以定期清理，这里暂时不实现
    }
}
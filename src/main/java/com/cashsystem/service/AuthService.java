package com.cashsystem.service;

public interface AuthService {

    /**
     * 验证Token是否有效
     */
    boolean validateToken(String token);

    /**
     * 从Token中获取用户ID
     */
    Long getUserIdFromToken(String token);

    /**
     * 从Token中获取用户名
     */
    String getUsernameFromToken(String token);

    /**
     * 从Token中获取用户角色
     */
    String getRoleFromToken(String token);
}
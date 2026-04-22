package com.cashsystem.vo;

import com.cashsystem.entity.User;
import lombok.Data;

@Data
public class LoginResultVO {
    private boolean success;
    private String message;
    private String token;
    private User user;

    public LoginResultVO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResultVO(boolean success, String message, String token, User user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.user = user;
    }

    public static LoginResultVO success(String message, String token, User user) {
        return new LoginResultVO(true, message, token, user);
    }

    public static LoginResultVO error(String message) {
        return new LoginResultVO(false, message);
    }
}
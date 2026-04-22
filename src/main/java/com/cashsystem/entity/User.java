package com.cashsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String account;
    private String password;
    private String accountType; // admin/operator
    private Long clientId;
    private String role;
    private String fullName;
    private String contact;
    private Integer loginTimes;
    private LocalDateTime lastLogin;
    private String status;
    private String avatar;

    // 构造函数
    public User() {}

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }
}
package com.cashsystem.entity;

import lombok.Data;

@Data
public class CleaningOperator {
    private Long id;
    private Long clientId;
    private String account;
    private String password;
    private String name;
    private String mobile;
    private String gender;
    private String permissions; // JSON格式
    private String status;
}
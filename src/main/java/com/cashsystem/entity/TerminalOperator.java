package com.cashsystem.entity;

import lombok.Data;

@Data
public class TerminalOperator {
    private Long id;
    private Long clientId;
    private Long branchId;
    private String account;
    private String password;
    private String name;
    private String mobile;
    private String gender; // 男/女
    private String userType;
    private Long settlementAccountId;
    private String permissions; // JSON格式
    private String status;
}
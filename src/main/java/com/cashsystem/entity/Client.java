package com.cashsystem.entity;

import lombok.Data;

@Data
public class Client {
    private Long id;
    private String clientName;
    private String contactPerson;
    private String contactPhone;
    private String address;
    private String status; // 启用/停用
}
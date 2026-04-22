package com.cashsystem.entity;

import lombok.Data;

@Data
public class DeviceLock {
    private Long id;
    private Long deviceId;
    private String snNumber;
    private String lockType;
    private String clientNumber;
    private String authCode;
    private Integer battery; // 电量百分比
    private String lockStatus; // 开/关/异常
    private String status; // 启用/停用
}
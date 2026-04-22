package com.cashsystem.entity;

import lombok.Data;

@Data
public class Device {
    private Long id;
    private Long branchId;
    private String deviceCode;
    private String deviceName;
    private String appid; // 保持原样，忽略拼写警告
    private String secret;
    private String lockSn;
    private Integer cashPreset;
    private String softwareVersion;
    private String status; // 运行/停用/故障
}
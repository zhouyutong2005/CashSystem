package com.cashsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class SystemTask {
    private Long id;
    private Long clientId;
    private String taskType; // 清机/对账/报表
    private String taskName;
    private LocalTime dailyExecTime;
    private Integer priority; // 1-10
    private Boolean isRealtime;
    private String status; // 启用/停用
    private LocalDateTime scheduledTime;
    private String parameters; // JSON格式
}
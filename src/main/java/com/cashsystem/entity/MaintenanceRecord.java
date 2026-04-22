package com.cashsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MaintenanceRecord {
    private Long id;
    private Long deviceId;
    private String maintenanceType; // 日常维护/故障维修
    private Long operatorId;
    private String status; // 进行中/已完成
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
}
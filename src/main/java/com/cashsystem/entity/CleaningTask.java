package com.cashsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CleaningTask {
    private Long id;
    private Long deviceId;
    private Long operatorAId;
    private Long operatorBId;
    private LocalDateTime plannedStartTime;
    private LocalDateTime plannedEndTime;
    private String taskStatus; // 待执行/执行中/已完成
    private String lockSn;
}
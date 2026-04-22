package com.cashsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PushMessage {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // 启用/停用
    private Long clientId;
    private Long branchId;
}
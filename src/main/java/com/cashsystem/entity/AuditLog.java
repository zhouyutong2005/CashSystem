package com.cashsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLog {
    private Long id;
    private Long accountId;
    private String module;
    private String action;
    private String apiPath;
    private String ip;
    private LocalDateTime actionTime;
}
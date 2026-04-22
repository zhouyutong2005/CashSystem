package com.cashsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditRecord {
    private Long id;
    private Long depositRecordId;
    private Long auditorId;
    private LocalDateTime auditTime;
    private String auditResult; // 通过/拒绝
    private String remark;
}
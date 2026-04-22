package com.cashsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DailySettlement {
    private Long id;
    private Long clientId;
    private BigDecimal total;
    private BigDecimal adjustment;
    private String type; // 正常/调整
    private String status; // 待审核/已审核
    private String batchNumber;
    private LocalDateTime auditTime;
    private LocalDate settlementDate;
}
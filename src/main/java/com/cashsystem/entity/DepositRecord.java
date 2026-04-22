package com.cashsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DepositRecord {
    private Long id;
    private String depositNo;
    private Long deviceId;
    private Long terminalOperatorId;
    private String account;
    private Long depositTypeId;
    private String currency; // CNY/USD
    private BigDecimal cashAmount;
    private Integer cashCount;
    private BigDecimal envelopeAmount;
    private BigDecimal total;
    private String cleaningBatch;
    private String settlementBatch;
    private String auditStatus; // 待审核/通过/拒绝
    private LocalDateTime depositTime;
    private String remark;
}
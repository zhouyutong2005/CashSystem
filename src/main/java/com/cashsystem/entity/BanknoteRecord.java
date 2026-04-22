package com.cashsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BanknoteRecord {
    private Long id;
    private Long depositRecordId;
    private String serialNumber; // 冠字号
    private String currency; // CNY/USD
    private BigDecimal denomination; // 面额
    private LocalDateTime recordTime;
}
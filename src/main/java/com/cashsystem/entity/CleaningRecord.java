package com.cashsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CleaningRecord {
    private Long id;
    private Long deviceId;
    private String oldBagNumber;
    private String newBagNumber;
    private String oldBatchNumber;
    private String newBatchNumber;
    private BigDecimal totalAmount;
    private String storageStatus; // 待入库/已入库
    private LocalDate clearingDate;
    private Long operatorId;
}
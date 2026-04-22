package com.cashsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CashInventory {
    private Long id;
    private Long deviceId;
    private String currency; // CNY/USD
    private BigDecimal denomination;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String bagNumber;
    private LocalDateTime updatedTime;
}
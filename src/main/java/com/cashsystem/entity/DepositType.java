package com.cashsystem.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DepositType {
    private Long id;
    private Long clientId;
    private String typeCode;
    private String typeName;
    private String description;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String currency; // CNY/USD
    private String status;
}
package com.cashsystem.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SettlementAccount {
    private Long id;
    private Long clientId;
    private String accountName;
    private String bankName;
    private String accountNumber;
    private String bankCode;
    private String accountType; // 对公/对私
    private BigDecimal balance;
    private Boolean isDefault;
}
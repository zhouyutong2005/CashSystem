package com.cashsystem.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Branch {
    private Long id;
    private Long clientId;
    private String branchName;
    private BigDecimal dailyAlertAmount;
    private String address;
    private String status;
}
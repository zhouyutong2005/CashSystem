package com.cashsystem.service;

import com.cashsystem.entity.DailySettlement;
import java.time.LocalDate;
import java.util.List;

public interface DailySettlementService {

    /**
     * 创建日结记录
     */
    boolean createDailySettlement(DailySettlement settlement);

    /**
     * 根据ID查询日结记录
     */
    DailySettlement findById(Long id);

    /**
     * 查询客户指定日期的日结记录
     */
    DailySettlement findByClientAndDate(Long clientId, LocalDate date);

    /**
     * 查询客户日期范围内的日结记录
     */
    List<DailySettlement> findByClientAndDateRange(Long clientId, LocalDate startDate, LocalDate endDate);

    /**
     * 更新日结状态
     */
    boolean updateSettlementStatus(Long settlementId, String status);

    /**
     * 执行日结操作
     */
    boolean executeDailySettlement(Long clientId, LocalDate settlementDate);
}
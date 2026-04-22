package com.cashsystem.service.impl;

import com.cashsystem.entity.DailySettlement;
import com.cashsystem.mapper.DailySettlementMapper;
import com.cashsystem.service.DailySettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailySettlementServiceImpl implements DailySettlementService {

    private final DailySettlementMapper dailySettlementMapper;

    @Override
    @Transactional
    public boolean createDailySettlement(DailySettlement settlement) {
        try {
            // 生成批次号
            String batchNumber = "DS" + System.currentTimeMillis();
            settlement.setBatchNumber(batchNumber);

            // 设置默认状态
            if (settlement.getStatus() == null) {
                settlement.setStatus("待审核");
            }

            // 设置结算日期
            if (settlement.getSettlementDate() == null) {
                settlement.setSettlementDate(LocalDate.now());
            }

            return dailySettlementMapper.insert(settlement) > 0;
        } catch (Exception e) {
            log.error("创建日结记录失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public DailySettlement findById(Long id) {
        return dailySettlementMapper.findById(id);
    }

    @Override
    public DailySettlement findByClientAndDate(Long clientId, LocalDate date) {
        return dailySettlementMapper.findByClientAndDate(clientId, date);
    }

    @Override
    public List<DailySettlement> findByClientAndDateRange(Long clientId, LocalDate startDate, LocalDate endDate) {
        return dailySettlementMapper.findByClientAndDateRange(clientId, startDate, endDate);
    }

    @Override
    @Transactional
    public boolean updateSettlementStatus(Long settlementId, String status) {
        try {
            return dailySettlementMapper.updateStatus(settlementId, status) > 0;
        } catch (Exception e) {
            log.error("更新日结状态失败: settlementId={}, status={}", settlementId, status, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean executeDailySettlement(Long clientId, LocalDate settlementDate) {
        try {
            // 1. 检查是否已存在日结记录
            DailySettlement existing = dailySettlementMapper.findByClientAndDate(clientId, settlementDate);
            if (existing != null) {
                log.warn("日结记录已存在: clientId={}, date={}", clientId, settlementDate);
                return false;
            }

            // 2. 创建新的日结记录（这里简化处理，实际应该计算总金额）
            DailySettlement settlement = new DailySettlement();
            settlement.setClientId(clientId);
            settlement.setSettlementDate(settlementDate);
            settlement.setTotal(BigDecimal.ZERO); // 实际应该从存款记录统计
            settlement.setAdjustment(BigDecimal.ZERO);
            settlement.setType("正常");
            settlement.setStatus("待审核");

            return createDailySettlement(settlement);
        } catch (Exception e) {
            log.error("执行日结失败: clientId={}, date={}", clientId, settlementDate, e);
            return false;
        }
    }
}
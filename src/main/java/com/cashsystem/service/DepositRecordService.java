package com.cashsystem.service;

import com.cashsystem.entity.DepositRecord;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DepositRecordService {

    /**
     * 创建存款记录
     */
    boolean createDepositRecord(DepositRecord record);

    /**
     * 根据ID查询存款记录
     */
    DepositRecord findById(Long id);

    /**
     * 根据流水号查询存款记录
     */
    DepositRecord findByDepositNo(String depositNo);

    /**
     * 查询操作员的存款记录
     */
    List<DepositRecord> findByOperatorAndTimeRange(Long operatorId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询设备的存款记录
     */
    List<DepositRecord> findByDeviceAndTime(Long deviceId, LocalDateTime startTime);

    /**
     * 更新审核状态
     */
    boolean updateAuditStatus(Long recordId, String auditStatus, String cleaningBatch);

    /**
     * 获取存款统计
     */
    Map<String, Object> getDepositStatistics(LocalDateTime startTime, LocalDateTime endTime, Long deviceId);
}
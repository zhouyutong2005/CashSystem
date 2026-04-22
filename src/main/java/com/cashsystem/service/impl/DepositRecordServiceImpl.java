package com.cashsystem.service.impl;

import com.cashsystem.entity.DepositRecord;
import com.cashsystem.mapper.DepositRecordMapper;
import com.cashsystem.service.DepositRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositRecordServiceImpl implements DepositRecordService {

    private final DepositRecordMapper depositRecordMapper;

    @Override
    @Transactional
    public boolean createDepositRecord(DepositRecord record) {
        try {
            // 生成存款流水号（这里简化处理，实际应该用序列号生成器）
            String depositNo = "D" + System.currentTimeMillis();
            record.setDepositNo(depositNo);

            // 设置默认审核状态
            if (record.getAuditStatus() == null) {
                record.setAuditStatus("待审核");
            }

            // 计算总额
            BigDecimal cashAmount = record.getCashAmount() != null ? record.getCashAmount() : BigDecimal.ZERO;
            BigDecimal envelopeAmount = record.getEnvelopeAmount() != null ? record.getEnvelopeAmount() : BigDecimal.ZERO;
            record.setTotal(cashAmount.add(envelopeAmount));

            // 设置存款时间
            if (record.getDepositTime() == null) {
                record.setDepositTime(LocalDateTime.now());
            }

            return depositRecordMapper.insert(record) > 0;
        } catch (Exception e) {
            log.error("创建存款记录失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public DepositRecord findById(Long id) {
        return depositRecordMapper.findById(id);
    }

    @Override
    public DepositRecord findByDepositNo(String depositNo) {
        return depositRecordMapper.findByDepositNo(depositNo);
    }

    @Override
    public List<DepositRecord> findByOperatorAndTimeRange(Long operatorId, LocalDateTime startTime, LocalDateTime endTime) {
        return depositRecordMapper.findByOperatorAndTimeRange(operatorId, startTime, endTime);
    }

    @Override
    public List<DepositRecord> findByDeviceAndTime(Long deviceId, LocalDateTime startTime) {
        return depositRecordMapper.findByDeviceAndTime(deviceId, startTime);
    }

    @Override
    @Transactional
    public boolean updateAuditStatus(Long recordId, String auditStatus, String cleaningBatch) {
        try {
            return depositRecordMapper.updateAuditStatus(recordId, auditStatus, cleaningBatch) > 0;
        } catch (Exception e) {
            log.error("更新存款记录审核状态失败: recordId={}, status={}", recordId, auditStatus, e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getDepositStatistics(LocalDateTime startTime, LocalDateTime endTime, Long deviceId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            params.put("deviceId", deviceId);
            params.put("auditStatus", "通过");

            return depositRecordMapper.getDepositStatistics(params);
        } catch (Exception e) {
            log.error("获取存款统计失败: {}", e.getMessage());
            return new HashMap<>();
        }
    }
}
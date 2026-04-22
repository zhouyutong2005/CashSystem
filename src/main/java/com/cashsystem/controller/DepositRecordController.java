package com.cashsystem.controller;

import com.cashsystem.entity.DepositRecord;
import com.cashsystem.service.DepositRecordService;
import com.cashsystem.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/deposit-records")
@RequiredArgsConstructor
public class DepositRecordController {

    private final DepositRecordService depositRecordService;

    /**
     * 创建存款记录
     */
    @PostMapping
    public ResultVO<String> createDepositRecord(@RequestBody DepositRecord record) {
        try {
            boolean success = depositRecordService.createDepositRecord(record);
            if (success) {
                return ResultVO.success("创建存款记录成功", record.getDepositNo());
            } else {
                return ResultVO.error("创建存款记录失败");
            }
        } catch (Exception e) {
            log.error("创建存款记录失败: {}", e.getMessage());
            return ResultVO.error("创建存款记录失败");
        }
    }

    /**
     * 根据ID查询存款记录
     */
    @GetMapping("/{id}")
    public ResultVO<DepositRecord> getDepositRecordById(@PathVariable Long id) {
        try {
            DepositRecord record = depositRecordService.findById(id);
            if (record != null) {
                return ResultVO.success("获取存款记录成功", record);
            } else {
                return ResultVO.error("存款记录不存在");
            }
        } catch (Exception e) {
            log.error("查询存款记录失败: id={}", id, e);
            return ResultVO.error("查询存款记录失败");
        }
    }

    /**
     * 根据流水号查询存款记录
     */
    @GetMapping("/no/{depositNo}")
    public ResultVO<DepositRecord> getDepositRecordByNo(@PathVariable String depositNo) {
        try {
            DepositRecord record = depositRecordService.findByDepositNo(depositNo);
            if (record != null) {
                return ResultVO.success("获取存款记录成功", record);
            } else {
                return ResultVO.error("存款记录不存在");
            }
        } catch (Exception e) {
            log.error("查询存款记录失败: depositNo={}", depositNo, e);
            return ResultVO.error("查询存款记录失败");
        }
    }

    /**
     * 查询操作员的存款记录
     */
    @GetMapping("/operator/{operatorId}")
    public ResultVO<List<DepositRecord>> getDepositRecordsByOperator(
            @PathVariable Long operatorId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        try {
            LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime) : LocalDateTime.now().minusDays(7);
            LocalDateTime end = endTime != null ? LocalDateTime.parse(endTime) : LocalDateTime.now();

            List<DepositRecord> records = depositRecordService.findByOperatorAndTimeRange(operatorId, start, end);
            return ResultVO.success("获取存款记录成功", records);
        } catch (Exception e) {
            log.error("查询存款记录失败: operatorId={}", operatorId, e);
            return ResultVO.error("查询存款记录失败");
        }
    }

    /**
     * 更新审核状态
     */
    @PutMapping("/{id}/audit-status")
    public ResultVO<String> updateAuditStatus(@PathVariable Long id,
                                              @RequestParam String auditStatus,
                                              @RequestParam(required = false) String cleaningBatch) {
        try {
            boolean success = depositRecordService.updateAuditStatus(id, auditStatus, cleaningBatch);
            if (success) {
                return ResultVO.success("更新审核状态成功", null);
            } else {
                return ResultVO.error("更新审核状态失败");
            }
        } catch (Exception e) {
            log.error("更新审核状态失败: id={}, status={}", id, auditStatus, e);
            return ResultVO.error("更新审核状态失败");
        }
    }

    /**
     * 获取存款统计
     */
    @GetMapping("/statistics")
    public ResultVO<Map<String, Object>> getDepositStatistics(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Long deviceId) {
        try {
            LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime) : LocalDateTime.now().minusDays(30);
            LocalDateTime end = endTime != null ? LocalDateTime.parse(endTime) : LocalDateTime.now();

            Map<String, Object> statistics = depositRecordService.getDepositStatistics(start, end, deviceId);
            return ResultVO.success("获取统计成功", statistics);
        } catch (Exception e) {
            log.error("获取存款统计失败: {}", e.getMessage());
            return ResultVO.error("获取存款统计失败");
        }
    }
}
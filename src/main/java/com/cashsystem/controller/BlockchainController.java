package com.cashsystem.controller;

import com.cashsystem.service.BlockchainService;
import com.cashsystem.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/blockchain")
@RequiredArgsConstructor
public class BlockchainController {

    private final BlockchainService blockchainService;

    /**
     * 手动触发数据存证
     */
    @PostMapping("/store")
    public ResultVO<Object> storeData(@RequestParam String businessType,
                                      @RequestParam Long businessId,
                                      @RequestBody Object data) {
        try {
            var record = blockchainService.storeData(businessType, businessId, data);
            return ResultVO.success("存证请求已提交", record);
        } catch (Exception e) {
            log.error("手动存证失败: businessType={}, businessId={}", businessType, businessId, e);
            return ResultVO.error("存证失败: " + e.getMessage());
        }
    }

    /**
     * 验证数据完整性
     */
    @PostMapping("/verify")
    public ResultVO<Boolean> verifyData(@RequestParam String businessType,
                                        @RequestParam Long businessId,
                                        @RequestBody Object data) {
        try {
            boolean verified = blockchainService.verifyData(businessType, businessId, data);
            if (verified) {
                return ResultVO.success("数据验证通过", true);
            } else {
                return ResultVO.success("数据验证不通过", false);
            }
        } catch (Exception e) {
            log.error("数据验证失败: businessType={}, businessId={}", businessType, businessId, e);
            return ResultVO.error("验证失败: " + e.getMessage());
        }
    }

    /**
     * 查询存证记录
     */
    @GetMapping("/record")
    public ResultVO<Object> getRecord(@RequestParam String businessType,
                                      @RequestParam Long businessId) {
        try {
            var record = blockchainService.getRecordByBusiness(businessType, businessId);
            if (record != null) {
                return ResultVO.success("查询成功", record);
            } else {
                return ResultVO.error("存证记录不存在");
            }
        } catch (Exception e) {
            log.error("查询存证记录失败: businessType={}, businessId={}", businessType, businessId, e);
            return ResultVO.error("查询失败");
        }
    }

    /**
     * 获取区块链统计信息
     */
    @GetMapping("/stats")
    public ResultVO<Map<String, Object>> getBlockchainStats() {
        try {
            Map<String, Object> stats = blockchainService.getBlockchainStats();
            return ResultVO.success("获取统计成功", stats);
        } catch (Exception e) {
            log.error("获取区块链统计失败", e);
            return ResultVO.error("获取统计失败");
        }
    }

    /**
     * 重试失败的存证记录
     */
    @PostMapping("/retry/{recordId}")
    public ResultVO<String> retryRecord(@PathVariable Long recordId) {
        try {
            boolean success = blockchainService.retryFailedRecord(recordId);
            if (success) {
                return ResultVO.success("重试成功", null);
            } else {
                return ResultVO.error("重试失败");
            }
        } catch (Exception e) {
            log.error("重试存证失败: recordId={}", recordId, e);
            return ResultVO.error("重试失败: " + e.getMessage());
        }
    }

    /**
     * 测试区块链连接
     */
    @GetMapping("/test")
    public ResultVO<String> testBlockchain() {
        try {
            // 测试存证一个简单数据
            Map<String, String> testData = Map.of("test", "blockchain", "timestamp", String.valueOf(System.currentTimeMillis()));
            var record = blockchainService.storeData("TEST", System.currentTimeMillis(), testData);

            if ("SUCCESS".equals(record.getStatus())) {
                return ResultVO.success("区块链服务正常", "模拟区块链连接成功，交易哈希: " + record.getTxHash());
            } else {
                return ResultVO.success("区块链服务异常", "模拟区块链交易失败");
            }
        } catch (Exception e) {
            log.error("区块链测试失败", e);
            return ResultVO.error("区块链测试失败: " + e.getMessage());
        }
    }
}
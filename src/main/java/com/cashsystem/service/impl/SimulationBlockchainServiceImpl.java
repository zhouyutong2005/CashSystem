package com.cashsystem.service.impl;

import com.cashsystem.entity.BlockchainRecord;
import com.cashsystem.mapper.BlockchainRecordMapper;
import com.cashsystem.service.BlockchainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationBlockchainServiceImpl implements BlockchainService {

    private final BlockchainRecordMapper blockchainRecordMapper;
    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    @Override
    @Transactional
    public BlockchainRecord storeData(String businessType, Long businessId, Object data) {
        try {
            log.info("开始区块链存证: businessType={}, businessId={}", businessType, businessId);

            // 1. 检查是否已存在存证记录
            BlockchainRecord existingRecord = blockchainRecordMapper.findByBusinessRecord(businessType, businessId);
            if (existingRecord != null) {
                log.warn("存证记录已存在: businessType={}, businessId={}", businessType, businessId);
                return existingRecord;
            }

            // 2. 计算数据哈希
            String dataJson = objectMapper.writeValueAsString(data);
            String dataHash = calculateSHA256(dataJson);

            // 3. 创建存证记录
            BlockchainRecord record = new BlockchainRecord();
            record.setBusinessType(businessType);
            record.setBusinessId(businessId);
            record.setDataHash(dataHash);
            record.setStatus("PENDING");
            record.setChainType("SIMULATION");
            record.setCreateTime(LocalDateTime.now());
            record.setUpdateTime(LocalDateTime.now());
            record.setRemark("模拟区块链存证");

            // 4. 保存到数据库
            blockchainRecordMapper.insert(record);

            // 5. 模拟区块链交易（90%成功率）
            boolean success = random.nextDouble() < 0.9;
            if (success) {
                // 模拟成功的区块链交易
                String txHash = generateRandomHash(64);
                String blockHash = generateRandomHash(64);
                long blockNumber = System.currentTimeMillis() / 1000;

                record.setTxHash(txHash);
                record.setBlockHash(blockHash);
                record.setBlockNumber(blockNumber);
                record.setStatus("SUCCESS");
                record.setUpdateTime(LocalDateTime.now());

                blockchainRecordMapper.updateBlockchainInfo(record);

                log.info("区块链存证成功: recordId={}, txHash={}", record.getId(), txHash);
            } else {
                // 模拟失败的交易
                record.setStatus("FAILED");
                record.setUpdateTime(LocalDateTime.now());
                record.setRemark("模拟区块链交易失败");

                blockchainRecordMapper.updateStatus(record.getId(), "FAILED");

                log.warn("区块链存证失败: recordId={}", record.getId());
            }

            return record;

        } catch (Exception e) {
            log.error("区块链存证异常: businessType={}, businessId={}", businessType, businessId, e);
            throw new RuntimeException("区块链存证失败", e);
        }
    }

    @Override
    public boolean verifyData(String businessType, Long businessId, Object data) {
        try {
            // 1. 查询存证记录
            BlockchainRecord record = blockchainRecordMapper.findByBusinessRecord(businessType, businessId);
            if (record == null || !"SUCCESS".equals(record.getStatus())) {
                log.warn("存证记录不存在或未成功: businessType={}, businessId={}", businessType, businessId);
                return false;
            }

            // 2. 计算当前数据哈希
            String dataJson = objectMapper.writeValueAsString(data);
            String currentHash = calculateSHA256(dataJson);

            // 3. 比较哈希值
            boolean verified = currentHash.equals(record.getDataHash());

            log.info("数据验证结果: businessType={}, businessId={}, verified={}",
                    businessType, businessId, verified);

            return verified;

        } catch (Exception e) {
            log.error("数据验证异常: businessType={}, businessId={}", businessType, businessId, e);
            return false;
        }
    }

    @Override
    public BlockchainRecord getRecordByBusiness(String businessType, Long businessId) {
        return blockchainRecordMapper.findByBusinessRecord(businessType, businessId);
    }

    @Override
    public BlockchainRecord getRecordByTxHash(String txHash) {
        return blockchainRecordMapper.findByTxHash(txHash);
    }

    @Override
    public Map<String, Object> getBlockchainStats() {
        Map<String, Object> stats = new HashMap<>();

        // 显式转换为 Long 类型，避免整数溢出和类型问题
        stats.put("totalRecords", (long) blockchainRecordMapper.countSuccessRecords("DEPOSIT") +
                (long) blockchainRecordMapper.countSuccessRecords("CLEANING") +
                (long) blockchainRecordMapper.countSuccessRecords("SETTLEMENT"));
        stats.put("depositRecords", (long) blockchainRecordMapper.countSuccessRecords("DEPOSIT"));
        stats.put("cleaningRecords", (long) blockchainRecordMapper.countSuccessRecords("CLEANING"));
        stats.put("settlementRecords", (long) blockchainRecordMapper.countSuccessRecords("SETTLEMENT"));
        stats.put("chainType", "SIMULATION");
        stats.put("status", "RUNNING");

        return stats;
    }

    @Override
    @Transactional
    public boolean retryFailedRecord(Long recordId) {
        try {
            BlockchainRecord record = blockchainRecordMapper.findById(recordId);
            if (record == null || !"FAILED".equals(record.getStatus())) {
                return false;
            }

            // 重新尝试存证（80%成功率）
            boolean success = random.nextDouble() < 0.8;
            if (success) {
                String txHash = generateRandomHash(64);
                String blockHash = generateRandomHash(64);
                long blockNumber = System.currentTimeMillis() / 1000;

                record.setTxHash(txHash);
                record.setBlockHash(blockHash);
                record.setBlockNumber(blockNumber);
                record.setStatus("SUCCESS");
                record.setUpdateTime(LocalDateTime.now());
                record.setRemark("重试成功");

                blockchainRecordMapper.updateBlockchainInfo(record);
                log.info("重试存证成功: recordId={}", recordId);
                return true;
            } else {
                record.setRemark("重试失败");
                record.setUpdateTime(LocalDateTime.now());
                blockchainRecordMapper.updateStatus(recordId, "FAILED");
                log.warn("重试存证失败: recordId={}", recordId);
                return false;
            }
        } catch (Exception e) {
            log.error("重试存证异常: recordId={}", recordId, e);
            return false;
        }
    }

    /**
     * 计算SHA256哈希
     */
    private String calculateSHA256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("计算哈希失败", e);
        }
    }

    /**
     * 生成随机哈希字符串
     */
    private String generateRandomHash(int length) {
        String characters = "0123456789abcdef";
        StringBuilder hash = new StringBuilder();
        for (int i = 0; i < length; i++) {
            hash.append(characters.charAt(random.nextInt(characters.length())));
        }
        return hash.toString();
    }
}
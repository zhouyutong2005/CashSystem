package com.cashsystem.service;

import com.cashsystem.entity.BlockchainRecord;
import java.util.Map;

public interface BlockchainService {

    /**
     * 数据存证 - 将关键业务数据上链
     */
    BlockchainRecord storeData(String businessType, Long businessId, Object data);

    /**
     * 验证数据 - 验证链上数据与本地数据是否一致
     */
    boolean verifyData(String businessType, Long businessId, Object data);

    /**
     * 查询存证记录
     */
    BlockchainRecord getRecordByBusiness(String businessType, Long businessId);

    /**
     * 查询存证记录By交易哈希
     */
    BlockchainRecord getRecordByTxHash(String txHash);

    /**
     * 获取区块链统计信息
     */
    Map<String, Object> getBlockchainStats();

    /**
     * 重新同步失败的存证记录
     */
    boolean retryFailedRecord(Long recordId);
}
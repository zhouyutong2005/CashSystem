package com.cashsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BlockchainRecord {
    private Long id;
    private String businessType; // 业务类型：DEPOSIT-存款, CLEANING-清机, SETTLEMENT-日结
    private Long businessId;     // 业务记录ID
    private String dataHash;     // 数据哈希值
    private String txHash;       // 交易哈希（区块链返回）
    private String blockHash;    // 区块哈希
    private Long blockNumber;    // 区块号
    private String status;       // 状态：PENDING-处理中, SUCCESS-成功, FAILED-失败
    private String chainType;    // 链类型：SIMULATION-模拟, ETHEREUM-以太坊, FABRIC-联盟链
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;       // 备注
}
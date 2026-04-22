package com.cashsystem.service;

import com.cashsystem.entity.BlockchainRecord;
import com.cashsystem.mapper.BlockchainRecordMapper;
import com.cashsystem.service.impl.SimulationBlockchainServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlockchainServiceTest {

    @Mock
    private BlockchainRecordMapper blockchainRecordMapper;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private SimulationBlockchainServiceImpl blockchainService;

    private Map<String, Object> testData;
    private BlockchainRecord testRecord;

    @BeforeEach
    void setUp() {
        testData = new HashMap<>();
        testData.put("amount", 1000.0);
        testData.put("currency", "CNY");
        testData.put("timestamp", "2025-01-01 12:00:00");

        testRecord = new BlockchainRecord();
        testRecord.setId(1L);
        testRecord.setBusinessType("DEPOSIT");
        testRecord.setBusinessId(1001L);
        testRecord.setDataHash("mock-data-hash");
        testRecord.setTxHash("mock-tx-hash");
        testRecord.setBlockHash("mock-block-hash");
        testRecord.setBlockNumber(12345L);
        testRecord.setStatus("SUCCESS");
        testRecord.setChainType("SIMULATION");
        testRecord.setCreateTime(LocalDateTime.now());
        testRecord.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void testStoreData_Success() throws Exception {
        // 准备测试数据
        when(blockchainRecordMapper.findByBusinessRecord("DEPOSIT", 1001L)).thenReturn(null);
        when(blockchainRecordMapper.insert(any(BlockchainRecord.class))).thenReturn(1);

        // 只设置实际会被调用的桩
        when(blockchainRecordMapper.updateBlockchainInfo(any(BlockchainRecord.class))).thenReturn(1);
        // 移除不必要的 updateStatus 桩，因为成功情况下不会调用它

        // 执行测试
        BlockchainRecord result = blockchainService.storeData("DEPOSIT", 1001L, testData);

        // 验证结果
        assertNotNull(result);
        assertEquals("DEPOSIT", result.getBusinessType());
        assertEquals(1001L, result.getBusinessId());
        assertNotNull(result.getDataHash());
        assertEquals("SUCCESS", result.getStatus()); // 验证状态是成功

        // 验证方法调用
        verify(blockchainRecordMapper).findByBusinessRecord("DEPOSIT", 1001L);
        verify(blockchainRecordMapper).insert(any(BlockchainRecord.class));
        verify(blockchainRecordMapper).updateBlockchainInfo(any(BlockchainRecord.class));
        // 验证 updateStatus 没有被调用
        verify(blockchainRecordMapper, never()).updateStatus(anyLong(), anyString());
    }

    @Test
    void testStoreData_RecordExists() {
        // 准备测试数据
        when(blockchainRecordMapper.findByBusinessRecord("DEPOSIT", 1001L)).thenReturn(testRecord);

        // 执行测试
        BlockchainRecord result = blockchainService.storeData("DEPOSIT", 1001L, testData);

        // 验证结果
        assertNotNull(result);
        assertEquals(testRecord, result);

        // 验证方法调用
        verify(blockchainRecordMapper).findByBusinessRecord("DEPOSIT", 1001L);
        verify(blockchainRecordMapper, never()).insert(any(BlockchainRecord.class));
    }

    @Test
    void testVerifyData_Success() throws Exception {
        // 准备测试数据 - 使用真实的数据计算哈希
        Map<String, Object> verificationData = new HashMap<>();
        verificationData.put("amount", 1000.0);
        verificationData.put("currency", "CNY");
        verificationData.put("timestamp", "2025-01-01 12:00:00");

        // 计算真实的数据哈希
        String dataJson = objectMapper.writeValueAsString(verificationData);
        String expectedHash = calculateSHA256(dataJson);

        BlockchainRecord verificationRecord = new BlockchainRecord();
        verificationRecord.setId(1L);
        verificationRecord.setBusinessType("DEPOSIT");
        verificationRecord.setBusinessId(1001L);
        verificationRecord.setDataHash(expectedHash); // 使用真实计算出的哈希
        verificationRecord.setTxHash("mock-tx-hash");
        verificationRecord.setStatus("SUCCESS");

        when(blockchainRecordMapper.findByBusinessRecord("DEPOSIT", 1001L)).thenReturn(verificationRecord);

        // 执行测试 - 使用相同的数据进行验证
        boolean result = blockchainService.verifyData("DEPOSIT", 1001L, verificationData);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(blockchainRecordMapper).findByBusinessRecord("DEPOSIT", 1001L);
    }

    /**
     * 计算SHA256哈希（与Service中相同的逻辑）
     */
    private String calculateSHA256(String data) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
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

    @Test
    void testVerifyData_RecordNotFound() {
        // 准备测试数据
        when(blockchainRecordMapper.findByBusinessRecord("DEPOSIT", 1001L)).thenReturn(null);

        // 执行测试
        boolean result = blockchainService.verifyData("DEPOSIT", 1001L, testData);

        // 验证结果
        assertFalse(result);
    }

    @Test
    void testVerifyData_DataMismatch() throws Exception {
        // 准备测试数据
        BlockchainRecord record = new BlockchainRecord();
        record.setDataHash("different-hash"); // 与测试数据不匹配的哈希
        record.setStatus("SUCCESS");

        when(blockchainRecordMapper.findByBusinessRecord("DEPOSIT", 1001L)).thenReturn(record);

        // 执行测试
        boolean result = blockchainService.verifyData("DEPOSIT", 1001L, testData);

        // 验证结果
        assertFalse(result);
    }

    @Test
    void testGetBlockchainStats() {
        // 准备测试数据
        when(blockchainRecordMapper.countSuccessRecords("DEPOSIT")).thenReturn(10);
        when(blockchainRecordMapper.countSuccessRecords("CLEANING")).thenReturn(5);
        when(blockchainRecordMapper.countSuccessRecords("SETTLEMENT")).thenReturn(3);

        // 执行测试
        Map<String, Object> stats = blockchainService.getBlockchainStats();

        // 验证结果
        assertNotNull(stats);
        assertEquals(18L, stats.get("totalRecords"));
        assertEquals(10L, stats.get("depositRecords"));
        assertEquals(5L, stats.get("cleaningRecords"));
        assertEquals(3L, stats.get("settlementRecords"));
        assertEquals("SIMULATION", stats.get("chainType"));
    }

    @Test
    void testRetryFailedRecord_Success() {
        // 准备测试数据
        BlockchainRecord failedRecord = new BlockchainRecord();
        failedRecord.setId(1L);
        failedRecord.setStatus("FAILED");

        when(blockchainRecordMapper.findById(1L)).thenReturn(failedRecord);
        when(blockchainRecordMapper.updateBlockchainInfo(any(BlockchainRecord.class))).thenReturn(1);

        // 执行测试
        boolean result = blockchainService.retryFailedRecord(1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(blockchainRecordMapper).findById(1L);
        verify(blockchainRecordMapper).updateBlockchainInfo(any(BlockchainRecord.class));
    }

    @Test
    void testRetryFailedRecord_RecordNotFound() {
        // 准备测试数据
        when(blockchainRecordMapper.findById(1L)).thenReturn(null);

        // 执行测试
        boolean result = blockchainService.retryFailedRecord(1L);

        // 验证结果
        assertFalse(result);
    }

    @Test
    void testRetryFailedRecord_NotFailed() {
        // 准备测试数据
        BlockchainRecord successRecord = new BlockchainRecord();
        successRecord.setId(1L);
        successRecord.setStatus("SUCCESS");

        when(blockchainRecordMapper.findById(1L)).thenReturn(successRecord);

        // 执行测试
        boolean result = blockchainService.retryFailedRecord(1L);

        // 验证结果
        assertFalse(result);
    }

    @Test
    void testGetRecordByBusiness_Success() {
        // 准备测试数据
        when(blockchainRecordMapper.findByBusinessRecord("DEPOSIT", 1001L)).thenReturn(testRecord);

        // 执行测试
        BlockchainRecord result = blockchainService.getRecordByBusiness("DEPOSIT", 1001L);

        // 验证结果
        assertNotNull(result);
        assertEquals(testRecord, result);
    }

    @Test
    void testGetRecordByBusiness_NotFound() {
        // 准备测试数据
        when(blockchainRecordMapper.findByBusinessRecord("DEPOSIT", 1001L)).thenReturn(null);

        // 执行测试
        BlockchainRecord result = blockchainService.getRecordByBusiness("DEPOSIT", 1001L);

        // 验证结果
        assertNull(result);
    }
}
package com.cashsystem.mapper;

import com.cashsystem.entity.DepositRecord;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface DepositRecordMapper {

    // 注解方式的简单查询
    @Select("SELECT * FROM deposit_records WHERE id = #{id}")
    DepositRecord findById(Long id);

    @Select("SELECT * FROM deposit_records WHERE deposit_no = #{depositNo}")
    DepositRecord findByDepositNo(String depositNo);

    @Select("SELECT * FROM deposit_records WHERE terminal_operator_id = #{operatorId} AND deposit_time BETWEEN #{startTime} AND #{endTime}")
    List<DepositRecord> findByOperatorAndTimeRange(Long operatorId, LocalDateTime startTime, LocalDateTime endTime);

    @Select("SELECT * FROM deposit_records WHERE device_id = #{deviceId} AND deposit_time >= #{startTime}")
    List<DepositRecord> findByDeviceAndTime(Long deviceId, LocalDateTime startTime);

    @Insert("INSERT INTO deposit_records(deposit_no, device_id, terminal_operator_id, account, deposit_type_id, currency, cash_amount, cash_count, envelope_amount, total, deposit_time, remark) " +
            "VALUES(#{depositNo}, #{deviceId}, #{terminalOperatorId}, #{account}, #{depositTypeId}, #{currency}, #{cashAmount}, #{cashCount}, #{envelopeAmount}, #{total}, #{depositTime}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DepositRecord record);

    @Update("UPDATE deposit_records SET audit_status = #{auditStatus}, cleaning_batch = #{cleaningBatch} WHERE id = #{id}")
    int updateAuditStatus(Long id, String auditStatus, String cleaningBatch);

    // XML方式的复杂查询
    List<DepositRecord> findByConditions(Map<String, Object> params);

    List<DepositRecord> findByPage(Map<String, Object> params);

    Long countByConditions(Map<String, Object> params);

    Map<String, Object> getDepositStatistics(Map<String, Object> params);

    BigDecimal getDailyTotal(@Param("date") LocalDate date,
                             @Param("auditStatus") String auditStatus,
                             @Param("deviceId") Long deviceId);

    List<Map<String, Object>> getDailyStatistics(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate,
                                                 @Param("deviceId") Long deviceId,
                                                 @Param("auditStatus") String auditStatus);

    List<Map<String, Object>> getStatisticsByDevice(@Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime,
                                                    @Param("auditStatus") String auditStatus);

    List<Map<String, Object>> getStatisticsByOperator(@Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime,
                                                      @Param("auditStatus") String auditStatus);

    List<DepositRecord> findLargeAmountRecords(@Param("minAmount") BigDecimal minAmount,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    int batchUpdateAuditStatus(@Param("ids") List<Long> ids,
                               @Param("auditStatus") String auditStatus,
                               @Param("cleaningBatch") String cleaningBatch);

    int batchUpdateCleaningBatch(@Param("ids") List<Long> ids,
                                 @Param("cleaningBatch") String cleaningBatch);

    int batchUpdateSettlementBatch(@Param("cleaningBatch") String cleaningBatch,
                                   @Param("settlementBatch") String settlementBatch);

    List<DepositRecord> findUnsettledRecords(@Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime,
                                             @Param("deviceId") Long deviceId);

    List<DepositRecord> findByCleaningBatch(String cleaningBatch);

    List<DepositRecord> findBySettlementBatch(String settlementBatch);

    List<Map<String, Object>> getCountStatistics(@Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime,
                                                 @Param("deviceId") Long deviceId);
}
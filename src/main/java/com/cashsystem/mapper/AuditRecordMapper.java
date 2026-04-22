package com.cashsystem.mapper;

import com.cashsystem.entity.AuditRecord;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AuditRecordMapper {

    @Select("SELECT * FROM audit_records WHERE id = #{id}")
    AuditRecord findById(Long id);

    @Select("SELECT * FROM audit_records WHERE deposit_record_id = #{depositRecordId}")
    AuditRecord findByDepositRecordId(Long depositRecordId);

    @Select("SELECT * FROM audit_records WHERE auditor_id = #{auditorId} AND audit_time BETWEEN #{startTime} AND #{endTime}")
    List<AuditRecord> findByAuditorAndTimeRange(Long auditorId, LocalDateTime startTime, LocalDateTime endTime);

    @Select("SELECT * FROM audit_records WHERE audit_result = #{auditResult} AND audit_time >= #{startTime}")
    List<AuditRecord> findByResultAndTime(String auditResult, LocalDateTime startTime);

    @Insert("INSERT INTO audit_records(deposit_record_id, auditor_id, audit_time, audit_result, remark) " +
            "VALUES(#{depositRecordId}, #{auditorId}, #{auditTime}, #{auditResult}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AuditRecord record);

    @Update("UPDATE audit_records SET audit_result = #{auditResult}, remark = #{remark} WHERE id = #{id}")
    int update(AuditRecord record);

    @Select("SELECT COUNT(*) FROM audit_records WHERE auditor_id = #{auditorId} AND DATE(audit_time) = CURDATE()")
    int countTodayAuditsByAuditor(Long auditorId);
}
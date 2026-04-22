package com.cashsystem.mapper;

import com.cashsystem.entity.AuditLog;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface AuditLogMapper {

    @Insert("INSERT INTO audit_logs(account_id, module, action, api_path, ip, action_time) " +
            "VALUES(#{accountId}, #{module}, #{action}, #{apiPath}, #{ip}, #{actionTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AuditLog auditLog);

    @Select("SELECT * FROM audit_logs WHERE account_id = #{accountId} AND action_time BETWEEN #{startTime} AND #{endTime} ORDER BY action_time DESC")
    List<AuditLog> findByAccountAndTimeRange(Long accountId, LocalDateTime startTime, LocalDateTime endTime);

    @Select("SELECT * FROM audit_logs WHERE module = #{module} AND action_time >= #{startTime} ORDER BY action_time DESC")
    List<AuditLog> findByModuleAndTime(String module, LocalDateTime startTime);

    @Select("SELECT * FROM audit_logs WHERE id = #{id}")
    AuditLog findById(Long id);

    @Select("SELECT * FROM audit_logs WHERE action LIKE CONCAT('%', #{actionKeyword}, '%')")
    List<AuditLog> findByActionKeyword(String actionKeyword);

    @Delete("DELETE FROM audit_logs WHERE action_time < #{time}")
    int deleteOldLogs(LocalDateTime time);
}
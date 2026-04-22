package com.cashsystem.mapper;

import com.cashsystem.entity.MaintenanceRecord;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MaintenanceRecordMapper {

    @Select("SELECT * FROM maintenance_records WHERE id = #{id}")
    MaintenanceRecord findById(Long id);

    @Select("SELECT * FROM maintenance_records WHERE device_id = #{deviceId} ORDER BY start_time DESC")
    List<MaintenanceRecord> findByDeviceId(Long deviceId);

    @Select("SELECT * FROM maintenance_records WHERE operator_id = #{operatorId} AND start_time BETWEEN #{startTime} AND #{endTime}")
    List<MaintenanceRecord> findByOperatorAndTimeRange(Long operatorId, LocalDateTime startTime, LocalDateTime endTime);

    @Select("SELECT * FROM maintenance_records WHERE status = '进行中'")
    List<MaintenanceRecord> findOngoingMaintenance();

    @Insert("INSERT INTO maintenance_records(device_id, maintenance_type, operator_id, status, start_time, description) " +
            "VALUES(#{deviceId}, #{maintenanceType}, #{operatorId}, #{status}, #{startTime}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(MaintenanceRecord record);

    @Update("UPDATE maintenance_records SET status = #{status}, end_time = #{endTime}, description = #{description} WHERE id = #{id}")
    int update(MaintenanceRecord record);

    @Update("UPDATE maintenance_records SET status = '已完成', end_time = NOW() WHERE id = #{id}")
    int completeMaintenance(Long id);
}
package com.cashsystem.mapper;

import com.cashsystem.entity.CleaningTask;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface CleaningTaskMapper {

    @Select("SELECT * FROM cleaning_tasks WHERE id = #{id}")
    CleaningTask findById(Long id);

    @Select("SELECT * FROM cleaning_tasks WHERE device_id = #{deviceId} AND task_status = '待执行'")
    List<CleaningTask> findPendingTasksByDeviceId(Long deviceId);

    @Select("SELECT * FROM cleaning_tasks WHERE operator_a_id = #{operatorId} OR operator_b_id = #{operatorId}")
    List<CleaningTask> findByOperatorId(Long operatorId);

    @Insert("INSERT INTO cleaning_tasks(device_id, operator_a_id, operator_b_id, planned_start_time, planned_end_time, task_status, lock_sn) " +
            "VALUES(#{deviceId}, #{operatorAId}, #{operatorBId}, #{plannedStartTime}, #{plannedEndTime}, #{taskStatus}, #{lockSn})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CleaningTask task);

    @Update("UPDATE cleaning_tasks SET task_status = #{taskStatus} WHERE id = #{id}")
    int updateStatus(Long id, String taskStatus);

    List<Map<String, Object>> findPendingTasks(@Param("branchId") Long branchId, @Param("operatorId") Long operatorId);
}
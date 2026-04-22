package com.cashsystem.mapper;

import com.cashsystem.entity.SystemTask;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SystemTaskMapper {

    @Select("SELECT * FROM system_tasks WHERE id = #{id}")
    SystemTask findById(Long id);

    @Select("SELECT * FROM system_tasks WHERE client_id = #{clientId} AND status = '启用'")
    List<SystemTask> findByClientId(Long clientId);

    @Select("SELECT * FROM system_tasks WHERE task_type = #{taskType} AND status = '启用'")
    List<SystemTask> findByTaskType(String taskType);

    @Select("SELECT * FROM system_tasks WHERE is_realtime = true AND status = '启用'")
    List<SystemTask> findRealTimeTasks();

    @Select("SELECT * FROM system_tasks WHERE scheduled_time <= #{time} AND status = '启用'")
    List<SystemTask> findScheduledTasks(LocalDateTime time);

    @Insert("INSERT INTO system_tasks(client_id, task_type, task_name, daily_exec_time, priority, is_realtime, status, scheduled_time, parameters) " +
            "VALUES(#{clientId}, #{taskType}, #{taskName}, #{dailyExecTime}, #{priority}, #{isRealtime}, #{status}, #{scheduledTime}, #{parameters})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SystemTask task);

    @Update("UPDATE system_tasks SET task_name=#{taskName}, daily_exec_time=#{dailyExecTime}, priority=#{priority}, status=#{status}, parameters=#{parameters} WHERE id=#{id}")
    int update(SystemTask task);

    @Update("UPDATE system_tasks SET scheduled_time = #{scheduledTime} WHERE id = #{id}")
    int updateScheduledTime(Long id, LocalDateTime scheduledTime);

    @Update("UPDATE system_tasks SET status = #{status} WHERE id = #{id}")
    int updateStatus(Long id, String status);
}
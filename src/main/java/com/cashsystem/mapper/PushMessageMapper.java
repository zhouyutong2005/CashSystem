package com.cashsystem.mapper;

import com.cashsystem.entity.PushMessage;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PushMessageMapper {

    @Select("SELECT * FROM push_messages WHERE id = #{id}")
    PushMessage findById(Long id);

    @Select("SELECT * FROM push_messages WHERE status = '启用' AND start_time <= NOW() AND (end_time IS NULL OR end_time >= NOW())")
    List<PushMessage> findActiveMessages();

    @Select("SELECT * FROM push_messages WHERE client_id = #{clientId} AND status = '启用'")
    List<PushMessage> findByClientId(Long clientId);

    @Select("SELECT * FROM push_messages WHERE branch_id = #{branchId} AND status = '启用'")
    List<PushMessage> findByBranchId(Long branchId);

    @Select("SELECT * FROM push_messages WHERE start_time BETWEEN #{startTime} AND #{endTime}")
    List<PushMessage> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    @Insert("INSERT INTO push_messages(title, content, start_time, end_time, status, client_id, branch_id) " +
            "VALUES(#{title}, #{content}, #{startTime}, #{endTime}, #{status}, #{clientId}, #{branchId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PushMessage message);

    @Update("UPDATE push_messages SET title=#{title}, content=#{content}, start_time=#{startTime}, end_time=#{endTime}, status=#{status} WHERE id=#{id}")
    int update(PushMessage message);

    @Update("UPDATE push_messages SET status = #{status} WHERE id = #{id}")
    int updateStatus(Long id, String status);

    @Delete("DELETE FROM push_messages WHERE end_time < #{time}")
    int deleteExpiredMessages(LocalDateTime time);
}
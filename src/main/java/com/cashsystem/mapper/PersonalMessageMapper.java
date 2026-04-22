package com.cashsystem.mapper;

import com.cashsystem.entity.PersonalMessage;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PersonalMessageMapper {

    @Select("SELECT * FROM personal_messages WHERE id = #{id}")
    PersonalMessage findById(Long id);

    @Select("SELECT * FROM personal_messages WHERE account_id = #{accountId} ORDER BY message_time DESC")
    List<PersonalMessage> findByAccountId(Long accountId);

    @Select("SELECT * FROM personal_messages WHERE account_id = #{accountId} AND is_read = false ORDER BY message_time DESC")
    List<PersonalMessage> findUnreadByAccountId(Long accountId);

    @Select("SELECT * FROM personal_messages WHERE message_type = #{messageType} AND message_time >= #{startTime}")
    List<PersonalMessage> findByTypeAndTime(String messageType, LocalDateTime startTime);

    @Select("SELECT COUNT(*) FROM personal_messages WHERE account_id = #{accountId} AND is_read = false")
    int countUnreadMessages(Long accountId);

    @Insert("INSERT INTO personal_messages(account_id, message_type, content, message_time, is_read) " +
            "VALUES(#{accountId}, #{messageType}, #{content}, #{messageTime}, #{isRead})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PersonalMessage message);

    @Update("UPDATE personal_messages SET is_read = true WHERE id = #{id}")
    int markAsRead(Long id);

    @Update("UPDATE personal_messages SET is_read = true WHERE account_id = #{accountId} AND is_read = false")
    int markAllAsRead(Long accountId);

    @Delete("DELETE FROM personal_messages WHERE id = #{id}")
    int delete(Long id);

    @Delete("DELETE FROM personal_messages WHERE message_time < #{time}")
    int deleteOldMessages(LocalDateTime time);
}
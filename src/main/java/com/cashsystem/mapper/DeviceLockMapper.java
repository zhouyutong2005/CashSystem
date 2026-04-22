package com.cashsystem.mapper;

import com.cashsystem.entity.DeviceLock;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeviceLockMapper {

    @Select("SELECT * FROM device_locks WHERE id = #{id}")
    DeviceLock findById(Long id);

    @Select("SELECT * FROM device_locks WHERE device_id = #{deviceId}")
    DeviceLock findByDeviceId(Long deviceId);

    @Select("SELECT * FROM device_locks WHERE sn_number = #{snNumber}")
    DeviceLock findBySnNumber(String snNumber);

    @Select("SELECT * FROM device_locks WHERE lock_status = #{lockStatus} AND status = '启用'")
    List<DeviceLock> findByLockStatus(String lockStatus);

    @Insert("INSERT INTO device_locks(device_id, sn_number, lock_type, client_number, auth_code, battery, lock_status, status) " +
            "VALUES(#{deviceId}, #{snNumber}, #{lockType}, #{clientNumber}, #{authCode}, #{battery}, #{lockStatus}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DeviceLock deviceLock);

    @Update("UPDATE device_locks SET lock_status = #{lockStatus}, battery = #{battery} WHERE id = #{id}")
    int updateLockStatus(Long id, String lockStatus, Integer battery);

    @Update("UPDATE device_locks SET status = #{status} WHERE id = #{id}")
    int updateStatus(Long id, String status);

    @Delete("DELETE FROM device_locks WHERE device_id = #{deviceId}")
    int deleteByDeviceId(Long deviceId);
}
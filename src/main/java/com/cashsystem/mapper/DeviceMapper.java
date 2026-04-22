package com.cashsystem.mapper;

import com.cashsystem.entity.Device;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeviceMapper {

    @Select("SELECT * FROM devices WHERE id = #{id}")
    Device findById(Long id);

    @Select("SELECT * FROM devices WHERE branch_id = #{branchId} AND status = '运行'")
    List<Device> findByBranchId(Long branchId);

    @Select("SELECT * FROM devices WHERE device_code = #{deviceCode}")
    Device findByDeviceCode(String deviceCode);

    @Insert("INSERT INTO devices(branch_id, device_code, device_name, appid, secret, lock_sn, cash_preset, software_version, status) " +
            "VALUES(#{branchId}, #{deviceCode}, #{deviceName}, #{appid}, #{secret}, #{lockSn}, #{cashPreset}, #{softwareVersion}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Device device);

    @Update("UPDATE devices SET device_name=#{deviceName}, cash_preset=#{cashPreset}, software_version=#{softwareVersion}, status=#{status} WHERE id=#{id}")
    int update(Device device);

    @Update("UPDATE devices SET status = #{status} WHERE id = #{id}")
    int updateStatus(Long id, String status);
}
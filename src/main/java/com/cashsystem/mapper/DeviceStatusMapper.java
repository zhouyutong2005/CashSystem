package com.cashsystem.mapper;

import com.cashsystem.entity.DeviceStatus;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DeviceStatusMapper {

    @Select("SELECT * FROM device_status WHERE device_id = #{deviceId}")
    DeviceStatus findByDeviceId(Long deviceId);

    @Insert("INSERT INTO device_status(device_id, sorter_status, sensor_status, camera_status, scanner_status, printer_status, cash_bag_status, safe_status, rfid_status, error_description, last_updated) " +
            "VALUES(#{deviceId}, #{sorterStatus}, #{sensorStatus}, #{cameraStatus}, #{scannerStatus}, #{printerStatus}, #{cashBagStatus}, #{safeStatus}, #{rfidStatus}, #{errorDescription}, #{lastUpdated})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DeviceStatus status);

    @Update("UPDATE device_status SET sorter_status=#{sorterStatus}, sensor_status=#{sensorStatus}, camera_status=#{cameraStatus}, " +
            "scanner_status=#{scannerStatus}, printer_status=#{printerStatus}, cash_bag_status=#{cashBagStatus}, safe_status=#{safeStatus}, " +
            "rfid_status=#{rfidStatus}, error_description=#{errorDescription}, last_updated=#{lastUpdated} WHERE device_id=#{deviceId}")
    int update(DeviceStatus status);
}
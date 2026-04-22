package com.cashsystem.service;

import com.cashsystem.entity.Device;
import java.util.List;

public interface DeviceService {

    /**
     * 根据ID查询设备
     */
    Device findById(Long id);

    /**
     * 根据网点ID查询设备
     */
    List<Device> findByBranchId(Long branchId);

    /**
     * 创建设备
     */
    boolean createDevice(Device device);

    /**
     * 更新设备信息
     */
    boolean updateDevice(Device device);

    /**
     * 更新设备状态
     */
    boolean updateDeviceStatus(Long deviceId, String status);

    /**
     * 根据设备编号查询设备
     */
    Device findByDeviceCode(String deviceCode);
}
package com.cashsystem.service.impl;

import com.cashsystem.entity.Device;
import com.cashsystem.mapper.DeviceMapper;
import com.cashsystem.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;

    @Override
    public Device findById(Long id) {
        return deviceMapper.findById(id);
    }

    @Override
    public List<Device> findByBranchId(Long branchId) {
        return deviceMapper.findByBranchId(branchId);
    }

    @Override
    @Transactional
    public boolean createDevice(Device device) {
        try {
            // 检查设备编号是否重复
            Device existingDevice = deviceMapper.findByDeviceCode(device.getDeviceCode());
            if (existingDevice != null) {
                return false;
            }

            // 设置默认状态
            if (device.getStatus() == null) {
                device.setStatus("运行");
            }

            return deviceMapper.insert(device) > 0;
        } catch (Exception e) {
            log.error("创建设备失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateDevice(Device device) {
        try {
            return deviceMapper.update(device) > 0;
        } catch (Exception e) {
            log.error("更新设备失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateDeviceStatus(Long deviceId, String status) {
        try {
            return deviceMapper.updateStatus(deviceId, status) > 0;
        } catch (Exception e) {
            log.error("更新设备状态失败: deviceId={}, status={}", deviceId, status, e);
            return false;
        }
    }

    @Override
    public Device findByDeviceCode(String deviceCode) {
        return deviceMapper.findByDeviceCode(deviceCode);
    }
}
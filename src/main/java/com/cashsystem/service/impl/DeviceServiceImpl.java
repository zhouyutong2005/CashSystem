package com.cashsystem.service.impl;

import com.cashsystem.entity.Device;
import com.cashsystem.mapper.DeviceMapper;
import com.cashsystem.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;

    /**
     * 根据 ID 查询设备
     * @Cacheable：第一次查数据库，结果存入 Redis；之后直接从 Redis 取，不再查数据库
     * key = "device:1"、"device:2" ...
     */
    @Override
    @Cacheable(value = "device", key = "#id")
    public Device findById(Long id) {
        log.debug("从数据库查询设备: id={}", id);
        return deviceMapper.findById(id);
    }

    /**
     * 根据网点 ID 查询设备列表
     * 设备列表不常变化，缓存 10 分钟（由 RedisCacheManager 统一配置）
     * key = "devices:branch:1"、"devices:branch:2" ...
     */
    @Override
    @Cacheable(value = "devices:branch", key = "#branchId")
    public List<Device> findByBranchId(Long branchId) {
        log.debug("从数据库查询网点设备列表: branchId={}", branchId);
        return deviceMapper.findByBranchId(branchId);
    }

    /**
     * 根据设备编号查询
     * key = "device:code:ATM001" ...
     */
    @Override
    @Cacheable(value = "device:code", key = "#deviceCode")
    public Device findByDeviceCode(String deviceCode) {
        log.debug("从数据库查询设备: deviceCode={}", deviceCode);
        return deviceMapper.findByDeviceCode(deviceCode);
    }

    /**
     * 创建设备
     * 创建后清除该网点的设备列表缓存，下次查询会重新从数据库加载
     */
    @Override
    @Transactional
    @CacheEvict(value = "devices:branch", key = "#device.branchId")
    public boolean createDevice(Device device) {
        try {
            Device existingDevice = deviceMapper.findByDeviceCode(device.getDeviceCode());
            if (existingDevice != null) {
                return false;
            }
            if (device.getStatus() == null) {
                device.setStatus("运行");
            }
            return deviceMapper.insert(device) > 0;
        } catch (Exception e) {
            log.error("创建设备失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 更新设备信息
     * 同时清除该设备的 id 缓存和 code 缓存
     */
    @Override
    @Transactional
    @CacheEvict(value = {"device", "device:code", "devices:branch"}, allEntries = true)
    public boolean updateDevice(Device device) {
        try {
            return deviceMapper.update(device) > 0;
        } catch (Exception e) {
            log.error("更新设备失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 更新设备状态
     * 状态变了，清除该设备的缓存
     */
    @Override
    @Transactional
    @CacheEvict(value = {"device", "devices:branch"}, allEntries = true)
    public boolean updateDeviceStatus(Long deviceId, String status) {
        try {
            return deviceMapper.updateStatus(deviceId, status) > 0;
        } catch (Exception e) {
            log.error("更新设备状态失败: deviceId={}, status={}", deviceId, status, e);
            return false;
        }
    }
}

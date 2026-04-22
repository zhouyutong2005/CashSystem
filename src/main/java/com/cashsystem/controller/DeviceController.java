package com.cashsystem.controller;

import com.cashsystem.entity.Device;
import com.cashsystem.service.DeviceService;
import com.cashsystem.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * 获取所有设备列表
     */
    @GetMapping
    public ResultVO<List<Device>> getAllDevices() {
        try {
            // 简化处理：返回测试数据
            List<Device> devices = deviceService.findByBranchId(1L); // 测试用
            return ResultVO.success("获取设备列表成功", devices);
        } catch (Exception e) {
            log.error("获取设备列表失败: {}", e.getMessage());
            return ResultVO.error("获取设备列表失败");
        }
    }

    /**
     * 根据ID查询设备
     */
    @GetMapping("/{id}")
    public ResultVO<Device> getDeviceById(@PathVariable Long id) {
        try {
            Device device = deviceService.findById(id);
            if (device != null) {
                return ResultVO.success("获取设备成功", device);
            } else {
                return ResultVO.error("设备不存在");
            }
        } catch (Exception e) {
            log.error("查询设备失败: id={}", id, e);
            return ResultVO.error("查询设备失败");
        }
    }

    /**
     * 创建设备
     */
    @PostMapping
    public ResultVO<String> createDevice(@RequestBody Device device) {
        try {
            boolean success = deviceService.createDevice(device);
            if (success) {
                return ResultVO.success("创建设备成功", null);
            } else {
                return ResultVO.error("创建设备失败，设备编号可能已存在");
            }
        } catch (Exception e) {
            log.error("创建设备失败: {}", e.getMessage());
            return ResultVO.error("创建设备失败");
        }
    }

    /**
     * 更新设备信息
     */
    @PutMapping("/{id}")
    public ResultVO<String> updateDevice(@PathVariable Long id, @RequestBody Device device) {
        try {
            device.setId(id);
            boolean success = deviceService.updateDevice(device);
            if (success) {
                return ResultVO.success("更新设备成功", null);
            } else {
                return ResultVO.error("更新设备失败");
            }
        } catch (Exception e) {
            log.error("更新设备失败: id={}", id, e);
            return ResultVO.error("更新设备失败");
        }
    }

    /**
     * 更新设备状态
     */
    @PutMapping("/{id}/status")
    public ResultVO<String> updateDeviceStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            boolean success = deviceService.updateDeviceStatus(id, status);
            if (success) {
                return ResultVO.success("更新设备状态成功", null);
            } else {
                return ResultVO.error("更新设备状态失败");
            }
        } catch (Exception e) {
            log.error("更新设备状态失败: id={}, status={}", id, status, e);
            return ResultVO.error("更新设备状态失败");
        }
    }

    /**
     * 根据设备编号查询设备
     */
    @GetMapping("/code/{deviceCode}")
    public ResultVO<Device> getDeviceByCode(@PathVariable String deviceCode) {
        try {
            Device device = deviceService.findByDeviceCode(deviceCode);
            if (device != null) {
                return ResultVO.success("获取设备成功", device);
            } else {
                return ResultVO.error("设备不存在");
            }
        } catch (Exception e) {
            log.error("查询设备失败: deviceCode={}", deviceCode, e);
            return ResultVO.error("查询设备失败");
        }
    }
}
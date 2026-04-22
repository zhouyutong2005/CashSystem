package com.cashsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeviceStatus {
    private Long id;
    private Long deviceId;
    private String sorterStatus; // 正常/异常
    private String sensorStatus;
    private String cameraStatus;
    private String scannerStatus;
    private String printerStatus;
    private String cashBagStatus; // 正常/满袋/异常
    private String safeStatus;
    private String rfidStatus;
    private String errorDescription;
    private LocalDateTime lastUpdated;
}
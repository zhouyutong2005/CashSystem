package com.cashsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PersonalMessage {
    private Long id;
    private Long accountId;
    private String messageType; // 通知/告警/提醒
    private String content;
    private LocalDateTime messageTime;
    private Boolean isRead;
}
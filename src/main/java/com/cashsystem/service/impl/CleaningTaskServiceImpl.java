package com.cashsystem.service.impl;

import com.cashsystem.entity.CleaningTask;
import com.cashsystem.mapper.CleaningTaskMapper;
import com.cashsystem.service.CleaningTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleaningTaskServiceImpl implements CleaningTaskService {

    private final CleaningTaskMapper cleaningTaskMapper;

    @Override
    @Transactional
    public boolean createCleaningTask(CleaningTask task) {
        try {
            // 设置默认状态
            if (task.getTaskStatus() == null) {
                task.setTaskStatus("待执行");
            }

            return cleaningTaskMapper.insert(task) > 0;
        } catch (Exception e) {
            log.error("创建清机任务失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public CleaningTask findById(Long id) {
        return cleaningTaskMapper.findById(id);
    }

    @Override
    public List<Map<String, Object>> findPendingTasks(Long branchId, Long operatorId) {
        return cleaningTaskMapper.findPendingTasks(branchId, operatorId);
    }

    @Override
    @Transactional
    public boolean updateTaskStatus(Long taskId, String status) {
        try {
            return cleaningTaskMapper.updateStatus(taskId, status) > 0;
        } catch (Exception e) {
            log.error("更新清机任务状态失败: taskId={}, status={}", taskId, status, e);
            return false;
        }
    }

    @Override
    public List<CleaningTask> findByOperatorId(Long operatorId) {
        return cleaningTaskMapper.findByOperatorId(operatorId);
    }
}
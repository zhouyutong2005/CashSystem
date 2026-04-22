package com.cashsystem.service;

import com.cashsystem.entity.CleaningTask;
import java.util.List;
import java.util.Map;

public interface CleaningTaskService {

    /**
     * 创建清机任务
     */
    boolean createCleaningTask(CleaningTask task);

    /**
     * 根据ID查询清机任务
     */
    CleaningTask findById(Long id);

    /**
     * 查询待执行的清机任务
     */
    List<Map<String, Object>> findPendingTasks(Long branchId, Long operatorId);

    /**
     * 更新任务状态
     */
    boolean updateTaskStatus(Long taskId, String status);

    /**
     * 根据操作员查询任务
     */
    List<CleaningTask> findByOperatorId(Long operatorId);
}
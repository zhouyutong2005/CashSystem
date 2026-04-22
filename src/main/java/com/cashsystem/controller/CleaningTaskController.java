package com.cashsystem.controller;

import com.cashsystem.entity.CleaningTask;
import com.cashsystem.service.CleaningTaskService;
import com.cashsystem.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/cleaning-tasks")
@RequiredArgsConstructor
public class CleaningTaskController {

    private final CleaningTaskService cleaningTaskService;

    /**
     * 创建清机任务
     */
    @PostMapping
    public ResultVO<String> createCleaningTask(@RequestBody CleaningTask task) {
        try {
            boolean success = cleaningTaskService.createCleaningTask(task);
            if (success) {
                return ResultVO.success("创建清机任务成功", null);
            } else {
                return ResultVO.error("创建清机任务失败");
            }
        } catch (Exception e) {
            log.error("创建清机任务失败: {}", e.getMessage());
            return ResultVO.error("创建清机任务失败");
        }
    }

    /**
     * 根据ID查询清机任务
     */
    @GetMapping("/{id}")
    public ResultVO<CleaningTask> getCleaningTaskById(@PathVariable Long id) {
        try {
            CleaningTask task = cleaningTaskService.findById(id);
            if (task != null) {
                return ResultVO.success("获取清机任务成功", task);
            } else {
                return ResultVO.error("清机任务不存在");
            }
        } catch (Exception e) {
            log.error("查询清机任务失败: id={}", id, e);
            return ResultVO.error("查询清机任务失败");
        }
    }

    /**
     * 查询待处理的清机任务
     */
    @GetMapping("/pending")
    public ResultVO<List<Map<String, Object>>> getPendingTasks(
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) Long operatorId) {
        try {
            List<Map<String, Object>> tasks = cleaningTaskService.findPendingTasks(branchId, operatorId);
            return ResultVO.success("获取待处理任务成功", tasks);
        } catch (Exception e) {
            log.error("查询待处理任务失败: branchId={}, operatorId={}", branchId, operatorId, e);
            return ResultVO.error("查询待处理任务失败");
        }
    }

    /**
     * 更新任务状态
     */
    @PutMapping("/{id}/status")
    public ResultVO<String> updateTaskStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            boolean success = cleaningTaskService.updateTaskStatus(id, status);
            if (success) {
                return ResultVO.success("更新任务状态成功", null);
            } else {
                return ResultVO.error("更新任务状态失败");
            }
        } catch (Exception e) {
            log.error("更新任务状态失败: id={}, status={}", id, status, e);
            return ResultVO.error("更新任务状态失败");
        }
    }
}
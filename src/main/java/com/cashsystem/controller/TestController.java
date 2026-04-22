package com.cashsystem.controller;

import com.cashsystem.entity.BanknoteRecord;
import com.cashsystem.mapper.BanknoteRecordMapper;
import com.cashsystem.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private BanknoteRecordMapper banknoteRecordMapper;

    @GetMapping
    public ResultVO<Map<String, Object>> test() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "现金缴存系统后端服务启动成功！");
        data.put("timestamp", System.currentTimeMillis());
        data.put("version", "1.0.0");
        return ResultVO.success("测试接口正常", data);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, 现金缴存系统！";
    }

    // 新增数据库测试接口
    @GetMapping("/database")
    public ResultVO<Map<String, Object>> testDatabase() {
        Map<String, Object> data = new HashMap<>();
        try {
            // 测试 count 方法
            Integer count = banknoteRecordMapper.countByDepositRecordId(1L);
            data.put("message", "数据库连接正常");
            data.put("count", count != null ? count : 0);
            data.put("timestamp", System.currentTimeMillis());
            return ResultVO.success("数据库测试成功", data);
        } catch (Exception e) {
            data.put("error", e.getMessage());
            data.put("errorType", e.getClass().getName());
            e.printStackTrace(); // 在控制台打印详细错误
            return ResultVO.error("数据库测试失败: " + e.getMessage());
        }
    }

    // 新增查询测试接口
    @GetMapping("/query")
    public ResultVO<Map<String, Object>> testQuery() {
        Map<String, Object> data = new HashMap<>();
        try {
            // 测试简单的查询方法
            BanknoteRecord record = banknoteRecordMapper.findById(1L);
            data.put("record", record);
            data.put("message", record != null ? "查询到数据" : "未查询到数据（这是正常的，ID=1的记录可能不存在）");
            data.put("timestamp", System.currentTimeMillis());
            return ResultVO.success("查询测试完成", data);
        } catch (Exception e) {
            data.put("error", e.getMessage());
            data.put("errorType", e.getClass().getName());
            e.printStackTrace();
            return ResultVO.error("查询测试失败: " + e.getMessage());
        }
    }
}
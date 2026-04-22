package com.cashsystem.mapper;

import com.cashsystem.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 测试后回滚数据
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    @Sql("/test-data.sql") // 使用SQL文件初始化测试数据
    void testFindByAccount() {
        // 执行测试
        User user = userMapper.findByAccount("testadmin");

        // 验证结果
        assertNotNull(user);
        assertEquals("testadmin", user.getAccount());
        assertEquals("测试管理员", user.getFullName());
        assertEquals("ADMIN", user.getRole());
        assertEquals("启用", user.getStatus());
    }

    @Test
    void testInsertUser() {
        // 准备测试数据
        User newUser = new User();
        newUser.setAccount("newtestuser");
        newUser.setPassword("testpassword");
        newUser.setFullName("新测试用户");
        newUser.setRole("OPERATOR");
        newUser.setStatus("启用");

        // 执行测试
        int result = userMapper.insert(newUser);

        // 验证结果
        assertEquals(1, result);
        assertNotNull(newUser.getId());

        // 验证数据是否正确插入
        User savedUser = userMapper.findByAccount("newtestuser");
        assertNotNull(savedUser);
        assertEquals("新测试用户", savedUser.getFullName());
    }

    @Test
    void testUpdateLoginInfo() {
        // 先插入测试用户
        User user = new User();
        user.setAccount("loginuser");
        user.setPassword("password");
        user.setFullName("登录用户");
        user.setRole("OPERATOR");
        user.setStatus("启用");
        user.setLoginTimes(0);
        userMapper.insert(user);

        // 执行测试
        userMapper.updateLoginInfo(user.getId());

        // 验证结果
        User updatedUser = userMapper.findByAccount("loginuser");
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getLoginTimes());
        assertNotNull(updatedUser.getLastLogin());
    }
}
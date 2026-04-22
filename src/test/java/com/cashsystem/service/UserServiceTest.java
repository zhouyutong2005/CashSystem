package com.cashsystem.service;

import com.cashsystem.entity.User;
import com.cashsystem.mapper.UserMapper;
import com.cashsystem.service.impl.UserServiceImpl;
import com.cashsystem.util.JwtUtil;
import com.cashsystem.vo.LoginResultVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setAccount("testuser");
        testUser.setPassword("password123");
        testUser.setFullName("测试用户");
        testUser.setRole("ADMIN");
        testUser.setStatus("启用");
        testUser.setLastLogin(LocalDateTime.now());
        testUser.setLoginTimes(5);
    }

    @Test
    void testLogin_Success() {
        // 准备测试数据
        when(userMapper.findByAccount("testuser")).thenReturn(testUser);
        when(jwtUtil.generateToken(anyLong(), anyString(), anyString())).thenReturn("mock-jwt-token");

        // 执行测试
        LoginResultVO result = userService.login("testuser", "password123");

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("登录成功", result.getMessage());
        assertEquals("mock-jwt-token", result.getToken());
        assertNotNull(result.getUser());

        // 验证方法调用
        verify(userMapper).findByAccount("testuser");
        verify(userMapper).updateLoginInfo(1L);
        verify(jwtUtil).generateToken(1L, "testuser", "ADMIN");
    }

    @Test
    void testLogin_UserNotFound() {
        // 准备测试数据
        when(userMapper.findByAccount("nonexistent")).thenReturn(null);

        // 执行测试
        LoginResultVO result = userService.login("nonexistent", "password123");

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("账号不存在", result.getMessage());

        // 验证方法调用
        verify(userMapper).findByAccount("nonexistent");
        verify(userMapper, never()).updateLoginInfo(anyLong());
    }

    @Test
    void testLogin_UserDisabled() {
        // 准备测试数据
        testUser.setStatus("停用");
        when(userMapper.findByAccount("testuser")).thenReturn(testUser);

        // 执行测试
        LoginResultVO result = userService.login("testuser", "password123");

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("账号已被禁用", result.getMessage());
    }

    @Test
    void testLogin_WrongPassword() {
        // 准备测试数据
        when(userMapper.findByAccount("testuser")).thenReturn(testUser);

        // 执行测试
        LoginResultVO result = userService.login("testuser", "wrongpassword");

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("密码错误", result.getMessage());
    }

    @Test
    void testFindById_Success() {
        // 准备测试数据
        when(userMapper.findById(1L)).thenReturn(testUser);

        // 执行测试
        User result = userService.findById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("testuser", result.getAccount());
        assertEquals("测试用户", result.getFullName());

        // 验证方法调用
        verify(userMapper).findById(1L);
    }

    @Test
    void testCreateUser_Success() {
        // 准备测试数据
        User newUser = new User();
        newUser.setAccount("newuser");
        newUser.setPassword("newpassword");
        newUser.setFullName("新用户");

        when(userMapper.findByAccount("newuser")).thenReturn(null);
        when(userMapper.insert(any(User.class))).thenReturn(1);

        // 执行测试
        boolean result = userService.createUser(newUser);

        // 验证结果
        assertTrue(result);
        assertEquals("启用", newUser.getStatus());

        // 验证方法调用
        verify(userMapper).findByAccount("newuser");
        verify(userMapper).insert(newUser);
    }

    @Test
    void testCreateUser_AccountExists() {
        // 准备测试数据
        User newUser = new User();
        newUser.setAccount("existinguser");

        when(userMapper.findByAccount("existinguser")).thenReturn(testUser);

        // 执行测试
        boolean result = userService.createUser(newUser);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(userMapper).findByAccount("existinguser");
        verify(userMapper, never()).insert(any(User.class));
    }
}
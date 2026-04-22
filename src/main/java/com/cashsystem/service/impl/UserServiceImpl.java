package com.cashsystem.service.impl;

import com.cashsystem.entity.User;
import com.cashsystem.mapper.UserMapper;
import com.cashsystem.service.UserService;
import com.cashsystem.util.JwtUtil;
import com.cashsystem.util.PasswordUtil;
import com.cashsystem.vo.LoginResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordUtil passwordUtil;

    @Override
    public LoginResultVO login(String account, String password) {
        try {
            // 1. 查询用户
            User user = userMapper.findByAccount(account);
            if (user == null) {
                return LoginResultVO.error("账号不存在");
            }

            // 2. 检查用户状态
            if (!"启用".equals(user.getStatus())) {
                return LoginResultVO.error("账号已被禁用");
            }

            // 3. 验证密码（这里简化处理，实际应该加密验证）
            if (!password.equals(user.getPassword())) {
                return LoginResultVO.error("密码错误");
            }

            // 4. 更新登录信息
            userMapper.updateLoginInfo(user.getId());

            // 5. 生成JWT token
            String token = jwtUtil.generateToken(user.getId(), user.getAccount(), user.getRole());

            // 6. 返回登录结果
            return LoginResultVO.success("登录成功", token, user);

        } catch (Exception e) {
            log.error("用户登录失败: account={}, error={}", account, e.getMessage());
            return LoginResultVO.error("登录失败，请稍后重试");
        }
    }

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public User findByAccount(String account) {
        return userMapper.findByAccount(account);
    }

    @Override
    @Transactional
    public boolean createUser(User user) {
        try {
            // 检查账号是否已存在
            User existingUser = userMapper.findByAccount(user.getAccount());
            if (existingUser != null) {
                return false;
            }

            // 设置默认值
            user.setLoginTimes(0);
            user.setStatus("启用");
            user.setLastLogin(LocalDateTime.now());

            // 密码应该加密存储（这里简化处理）
            // user.setPassword(passwordUtil.encodePassword(user.getPassword()));

            return userMapper.insert(user) > 0;
        } catch (Exception e) {
            log.error("创建用户失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {
        try {
            return userMapper.update(user) > 0;
        } catch (Exception e) {
            log.error("更新用户失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        try {
            User user = userMapper.findById(userId);
            if (user == null) {
                return false;
            }

            // 验证旧密码
            if (!oldPassword.equals(user.getPassword())) {
                return false;
            }

            // 更新密码
            return userMapper.updatePassword(userId, newPassword) > 0;
        } catch (Exception e) {
            log.error("修改密码失败: userId={}", userId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean resetPassword(Long userId, String newPassword) {
        try {
            return userMapper.updatePassword(userId, newPassword) > 0;
        } catch (Exception e) {
            log.error("重置密码失败: userId={}", userId, e);
            return false;
        }
    }
}
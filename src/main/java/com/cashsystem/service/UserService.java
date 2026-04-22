package com.cashsystem.service;

import com.cashsystem.entity.User;
import com.cashsystem.vo.LoginResultVO;

public interface UserService {

    /**
     * 用户登录
     */
    LoginResultVO login(String account, String password);

    /**
     * 根据ID查询用户
     */
    User findById(Long id);

    /**
     * 根据账号查询用户
     */
    User findByAccount(String account);

    /**
     * 创建用户
     */
    boolean createUser(User user);

    /**
     * 更新用户信息
     */
    boolean updateUser(User user);

    /**
     * 修改密码
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     */
    boolean resetPassword(Long userId, String newPassword);
}
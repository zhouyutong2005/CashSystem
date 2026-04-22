package com.cashsystem.controller;

import com.cashsystem.config.RequiresRole;
import com.cashsystem.entity.User;
import com.cashsystem.service.UserService;
import com.cashsystem.util.JwtUtil;
import com.cashsystem.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    @RequiresRole({"ADMIN", "MANAGER", "TERMINAL_OPERATOR", "CLEANING_OPERATOR"})
    public ResultVO<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
            User user = userService.findById(userId);
            if (user != null) {
                user.setPassword(null);
                return ResultVO.success("获取用户信息成功", user);
            } else {
                return ResultVO.error("用户不存在");
            }
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            return ResultVO.error("获取用户信息失败");
        }
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public ResultVO<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            if (user != null) {
                user.setPassword(null); // 隐藏密码
                return ResultVO.success("获取用户成功", user);
            } else {
                return ResultVO.error("用户不存在");
            }
        } catch (Exception e) {
            log.error("查询用户失败: id={}", id, e);
            return ResultVO.error("查询用户失败");
        }
    }

    /**
     * 创建用户
     */
    @PostMapping
    @RequiresRole({"ADMIN"})
    public ResultVO<String> createUser(@RequestBody User user) {
        try {
            boolean success = userService.createUser(user);
            if (success) {
                return ResultVO.success("创建用户成功", null);
            } else {
                return ResultVO.error("创建用户失败，账号可能已存在");
            }
        } catch (Exception e) {
            log.error("创建用户失败: {}", e.getMessage());
            return ResultVO.error("创建用户失败");
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    @RequiresRole({"ADMIN", "MANAGER"})
    public ResultVO<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            user.setId(id);
            boolean success = userService.updateUser(user);
            if (success) {
                return ResultVO.success("更新用户成功", null);
            } else {
                return ResultVO.error("更新用户失败");
            }
        } catch (Exception e) {
            log.error("更新用户失败: id={}", id, e);
            return ResultVO.error("更新用户失败");
        }
    }

    /**
     * 修改密码
     */
    @PutMapping("/{id}/password")
    public ResultVO<String> changePassword(@PathVariable Long id,
                                           @RequestParam String oldPassword,
                                           @RequestParam String newPassword) {
        try {
            boolean success = userService.changePassword(id, oldPassword, newPassword);
            if (success) {
                return ResultVO.success("修改密码成功", null);
            } else {
                return ResultVO.error("修改密码失败，旧密码错误或用户不存在");
            }
        } catch (Exception e) {
            log.error("修改密码失败: id={}", id, e);
            return ResultVO.error("修改密码失败");
        }
    }
}
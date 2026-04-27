package com.cashsystem.controller;

import com.cashsystem.dto.LoginDTO;
import com.cashsystem.service.UserService;
import com.cashsystem.service.impl.AuthServiceImpl;
import com.cashsystem.vo.LoginResultVO;
import com.cashsystem.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthServiceImpl authService;
    private final RedisTemplate<String, Object> redisTemplate;

    // 黑名单 key 前缀，和 JwtAuthenticationFilter 保持一致
    private static final String BLACKLIST_PREFIX = "token:blacklist:";

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResultVO<LoginResultVO> login(@Validated @RequestBody LoginDTO loginDTO) {
        try {
            log.info("用户登录请求: account={}", loginDTO.getAccount());
            LoginResultVO result = userService.login(loginDTO.getAccount(), loginDTO.getPassword());
            return ResultVO.success(result.getMessage(), result);
        } catch (Exception e) {
            log.error("登录处理异常: {}", e.getMessage());
            return ResultVO.error("登录失败，系统异常");
        }
    }

    /**
     * 退出登录
     * 将 token 加入黑名单，同时从 Redis 删除 token 信息
     * 即使 JWT 本身还没过期，也无法再使用
     */
    @PostMapping("/logout")
    public ResultVO<String> logout(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = bearerToken.replace("Bearer ", "");

            // 1. 从 Redis 删除 token 信息
            authService.removeToken(token);

            // 2. 将 token 加入黑名单，过期时间设为 JWT 剩余有效期
            //    这样即使有人拿着旧 token 来请求，也会被拦截
            redisTemplate.opsForValue().set(
                    BLACKLIST_PREFIX + token,
                    "1",
                    24, TimeUnit.HOURS  // 和 JWT 过期时间保持一致即可
            );

            log.info("用户退出登录成功");
            return ResultVO.success("退出登录成功", null);
        } catch (Exception e) {
            log.error("退出登录失败: {}", e.getMessage());
            return ResultVO.error("退出登录失败");
        }
    }

    /**
     * 测试接口
     */
    @GetMapping("/test")
    public ResultVO<String> test() {
        return ResultVO.success("认证服务正常", "Auth Service is working!");
    }
}
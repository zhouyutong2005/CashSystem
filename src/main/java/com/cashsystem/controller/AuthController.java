package com.cashsystem.controller;

import com.cashsystem.dto.LoginDTO;
import com.cashsystem.service.UserService;
import com.cashsystem.vo.LoginResultVO;
import com.cashsystem.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

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
     * 测试接口
     */
    @GetMapping("/test")
    public ResultVO<String> test() {
        return ResultVO.success("认证服务正常", "Auth Service is working!");
    }
}
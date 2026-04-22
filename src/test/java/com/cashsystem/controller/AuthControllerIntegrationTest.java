package com.cashsystem.controller;

import com.cashsystem.CashSystemApplication;
import com.cashsystem.dto.LoginDTO;
import com.cashsystem.entity.User;
import com.cashsystem.service.UserService;
import com.cashsystem.vo.LoginResultVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {CashSystemApplication.class, AuthControllerIntegrationTest.TestConfig.class})
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return org.mockito.Mockito.mock(UserService.class);
        }
    }

    @Test
    void testLogin_Success() throws Exception {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setAccount("admin");
        loginDTO.setPassword("123456");

        User user = new User();
        user.setId(1L);
        user.setAccount("admin");
        user.setFullName("管理员");
        user.setRole("ADMIN");
        user.setStatus("启用");

        LoginResultVO loginResult = LoginResultVO.success("登录成功", "mock-jwt-token", user);

        when(userService.login(anyString(), anyString())).thenReturn(loginResult);

        // 执行测试并验证
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("登录成功"))
                .andExpect(jsonPath("$.data.success").value(true))
                .andExpect(jsonPath("$.data.token").value("mock-jwt-token"));
    }
}
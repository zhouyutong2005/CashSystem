package com.cashsystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 公开接口
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/test",
                                "/api/test/**",
                                "/error"
                        ).permitAll()
                        // 管理员和管理者接口（宽松配置）
                        .requestMatchers(
                                "/api/users/**",
                                "/api/devices/**"
                        ).hasAnyRole("ADMIN", "MANAGER")
                        // 存款记录相关（终端操作员和管理员）
                        .requestMatchers(
                                "/api/deposit-records/**"
                        ).hasAnyRole("TERMINAL_OPERATOR", "ADMIN", "MANAGER")
                        // 清机任务相关（清机操作员和管理员）
                        .requestMatchers(
                                "/api/cleaning-tasks/**"
                        ).hasAnyRole("CLEANING_OPERATOR", "ADMIN", "MANAGER")
                        // 其他所有请求需要认证
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
package com.cashsystem.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// 修改这里：javax.servlet → jakarta.servlet
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class RoleAspect {

    @Around("@annotation(requiresRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequiresRole requiresRole) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("用户未认证");
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String[] requiredRoles = requiresRole.value();

        boolean hasRole = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> Arrays.stream(requiredRoles)
                        .anyMatch(requiredRole -> authority.equals("ROLE_" + requiredRole)));

        if (!hasRole) {
            String userRoles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            String requiredRoleStr = Arrays.stream(requiredRoles)
                    .map(role -> "ROLE_" + role)
                    .collect(Collectors.joining(", "));

            log.warn("权限不足: 用户角色={}, 需要的角色={}", userRoles, requiredRoleStr);
            throw new SecurityException("权限不足");
        }

        return joinPoint.proceed();
    }
}
package com.cashsystem.service.impl;

import com.cashsystem.entity.TerminalOperator;
import com.cashsystem.mapper.TerminalOperatorMapper;
import com.cashsystem.service.TerminalOperatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalOperatorServiceImpl implements TerminalOperatorService {

    private final TerminalOperatorMapper terminalOperatorMapper;

    @Override
    @Transactional
    public boolean createTerminalOperator(TerminalOperator operator) {
        try {
            // 检查账号是否已存在
            TerminalOperator existing = terminalOperatorMapper.findByAccount(operator.getAccount());
            if (existing != null) {
                return false;
            }

            // 设置默认状态
            if (operator.getStatus() == null) {
                operator.setStatus("启用");
            }

            return terminalOperatorMapper.insert(operator) > 0;
        } catch (Exception e) {
            log.error("创建终端操作员失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public TerminalOperator login(String account, String password) {
        try {
            TerminalOperator operator = terminalOperatorMapper.findByAccount(account);
            if (operator == null) {
                return null;
            }

            if (!"启用".equals(operator.getStatus())) {
                return null;
            }

            if (!password.equals(operator.getPassword())) {
                return null;
            }

            return operator;
        } catch (Exception e) {
            log.error("终端操作员登录失败: account={}", account, e);
            return null;
        }
    }

    @Override
    public List<TerminalOperator> findByBranchId(Long branchId) {
        return terminalOperatorMapper.findByBranchId(branchId);
    }

    @Override
    @Transactional
    public boolean updateTerminalOperator(TerminalOperator operator) {
        try {
            return terminalOperatorMapper.update(operator) > 0;
        } catch (Exception e) {
            log.error("更新终端操作员失败: {}", e.getMessage());
            return false;
        }
    }
}
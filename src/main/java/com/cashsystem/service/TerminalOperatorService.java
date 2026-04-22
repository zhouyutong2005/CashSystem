package com.cashsystem.service;

import com.cashsystem.entity.TerminalOperator;
import java.util.List;

public interface TerminalOperatorService {

    /**
     * 创建终端操作员
     */
    boolean createTerminalOperator(TerminalOperator operator);

    /**
     * 终端操作员登录
     */
    TerminalOperator login(String account, String password);

    /**
     * 根据网点ID查询操作员
     */
    List<TerminalOperator> findByBranchId(Long branchId);

    /**
     * 更新操作员信息
     */
    boolean updateTerminalOperator(TerminalOperator operator);
}
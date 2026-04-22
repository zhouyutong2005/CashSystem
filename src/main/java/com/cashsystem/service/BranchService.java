package com.cashsystem.service;

import com.cashsystem.entity.Branch;
import java.util.List;

public interface BranchService {

    /**
     * 创建网点
     */
    boolean createBranch(Branch branch);

    /**
     * 根据ID查询网点
     */
    Branch findById(Long id);

    /**
     * 根据客户ID查询网点
     */
    List<Branch> findByClientId(Long clientId);

    /**
     * 更新网点信息
     */
    boolean updateBranch(Branch branch);
}
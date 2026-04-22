package com.cashsystem.service.impl;

import com.cashsystem.entity.Branch;
import com.cashsystem.mapper.BranchMapper;
import com.cashsystem.service.BranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchMapper branchMapper;

    @Override
    @Transactional
    public boolean createBranch(Branch branch) {
        try {
            // 设置默认状态
            if (branch.getStatus() == null) {
                branch.setStatus("启用");
            }

            return branchMapper.insert(branch) > 0;
        } catch (Exception e) {
            log.error("创建网点失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Branch findById(Long id) {
        return branchMapper.findById(id);
    }

    @Override
    public List<Branch> findByClientId(Long clientId) {
        return branchMapper.findByClientId(clientId);
    }

    @Override
    @Transactional
    public boolean updateBranch(Branch branch) {
        try {
            return branchMapper.update(branch) > 0;
        } catch (Exception e) {
            log.error("更新网点失败: {}", e.getMessage());
            return false;
        }
    }
}
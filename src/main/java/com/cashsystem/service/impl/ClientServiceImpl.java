package com.cashsystem.service.impl;

import com.cashsystem.entity.Client;
import com.cashsystem.mapper.ClientMapper;
import com.cashsystem.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientMapper clientMapper;

    @Override
    @Transactional
    public boolean createClient(Client client) {
        try {
            // 设置默认状态
            if (client.getStatus() == null) {
                client.setStatus("启用");
            }

            return clientMapper.insert(client) > 0;
        } catch (Exception e) {
            log.error("创建客户失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Client findById(Long id) {
        return clientMapper.findById(id);
    }

    @Override
    public List<Client> findAllActive() {
        return clientMapper.findAllActive();
    }

    @Override
    @Transactional
    public boolean updateClient(Client client) {
        try {
            return clientMapper.update(client) > 0;
        } catch (Exception e) {
            log.error("更新客户失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean disableClient(Long clientId) {
        try {
            return clientMapper.disable(clientId) > 0;
        } catch (Exception e) {
            log.error("禁用客户失败: clientId={}", clientId, e);
            return false;
        }
    }
}
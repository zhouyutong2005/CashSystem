package com.cashsystem.service;

import com.cashsystem.entity.Client;
import java.util.List;

public interface ClientService {

    /**
     * 创建客户
     */
    boolean createClient(Client client);

    /**
     * 根据ID查询客户
     */
    Client findById(Long id);

    /**
     * 查询所有启用的客户
     */
    List<Client> findAllActive();

    /**
     * 更新客户信息
     */
    boolean updateClient(Client client);

    /**
     * 禁用客户
     */
    boolean disableClient(Long clientId);
}
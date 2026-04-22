-- 清理测试数据
DELETE FROM platform_accounts WHERE account LIKE 'test%';

-- 插入测试用户
INSERT INTO platform_accounts (account, password, account_type, role, full_name, contact, status, login_times, last_login)
VALUES
    ('testadmin', 'test123', 'admin', 'ADMIN', '测试管理员', '13800138000', '启用', 5, NOW()),
    ('testoperator', 'test123', 'operator', 'OPERATOR', '测试操作员', '13800138001', '启用', 3, NOW());
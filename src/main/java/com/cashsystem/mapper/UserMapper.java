package com.cashsystem.mapper;

import com.cashsystem.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    /**
     * 根据账号查询用户
     */
    @Select("SELECT * FROM platform_accounts WHERE account = #{account} AND status = '启用'")
    User findByAccount(String account);

    /**
     * 根据ID查询用户
     */
    @Select("SELECT * FROM platform_accounts WHERE id = #{id}")
    User findById(Long id);

    /**
     * 更新用户登录信息
     */
    @Update("UPDATE platform_accounts SET last_login = NOW(), login_times = login_times + 1 WHERE id = #{id}")
    void updateLoginInfo(Long id);

    /**
     * 插入新用户
     */
    @Insert("INSERT INTO platform_accounts(account, password, account_type, client_id, role, full_name, contact, status) " +
            "VALUES(#{account}, #{password}, #{accountType}, #{clientId}, #{role}, #{fullName}, #{contact}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    /**
     * 更新用户信息
     */
    @Update("UPDATE platform_accounts SET full_name=#{fullName}, contact=#{contact}, role=#{role}, status=#{status} WHERE id=#{id}")
    int update(User user);

    /**
     * 修改密码
     */
    @Update("UPDATE platform_accounts SET password = #{password} WHERE id = #{id}")
    int updatePassword(Long id, String password);
}
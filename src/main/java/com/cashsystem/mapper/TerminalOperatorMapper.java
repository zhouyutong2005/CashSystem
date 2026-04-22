package com.cashsystem.mapper;

import com.cashsystem.entity.TerminalOperator;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TerminalOperatorMapper {

    @Select("SELECT * FROM terminal_operators WHERE account = #{account} AND status = '启用'")
    TerminalOperator findByAccount(String account);

    @Select("SELECT * FROM terminal_operators WHERE branch_id = #{branchId} AND status = '启用'")
    List<TerminalOperator> findByBranchId(Long branchId);

    @Insert("INSERT INTO terminal_operators(client_id, branch_id, account, password, name, mobile, gender, user_type, settlement_account_id, permissions, status) " +
            "VALUES(#{clientId}, #{branchId}, #{account}, #{password}, #{name}, #{mobile}, #{gender}, #{userType}, #{settlementAccountId}, #{permissions}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TerminalOperator operator);

    @Update("UPDATE terminal_operators SET name=#{name}, mobile=#{mobile}, gender=#{gender}, user_type=#{userType}, status=#{status} WHERE id=#{id}")
    int update(TerminalOperator operator);
}
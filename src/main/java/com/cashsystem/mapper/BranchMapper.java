package com.cashsystem.mapper;

import com.cashsystem.entity.Branch;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BranchMapper {

    @Select("SELECT * FROM branches WHERE id = #{id}")
    Branch findById(Long id);

    @Select("SELECT * FROM branches WHERE client_id = #{clientId} AND status = '启用'")
    List<Branch> findByClientId(Long clientId);

    @Insert("INSERT INTO branches(client_id, branch_name, daily_alert_amount, address, status) " +
            "VALUES(#{clientId}, #{branchName}, #{dailyAlertAmount}, #{address}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Branch branch);

    @Update("UPDATE branches SET branch_name=#{branchName}, daily_alert_amount=#{dailyAlertAmount}, " +
            "address=#{address}, status=#{status} WHERE id=#{id}")
    int update(Branch branch);
}
package com.cashsystem.mapper;

import com.cashsystem.entity.CleaningOperator;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CleaningOperatorMapper {

    @Select("SELECT * FROM cleaning_operators WHERE account = #{account} AND status = '启用'")
    CleaningOperator findByAccount(String account);

    @Select("SELECT * FROM cleaning_operators WHERE client_id = #{clientId} AND status = '启用'")
    List<CleaningOperator> findByClientId(Long clientId);

    @Insert("INSERT INTO cleaning_operators(client_id, account, password, name, mobile, gender, permissions, status) " +
            "VALUES(#{clientId}, #{account}, #{password}, #{name}, #{mobile}, #{gender}, #{permissions}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CleaningOperator operator);

    @Update("UPDATE cleaning_operators SET name=#{name}, mobile=#{mobile}, gender=#{gender}, status=#{status} WHERE id=#{id}")
    int update(CleaningOperator operator);
}
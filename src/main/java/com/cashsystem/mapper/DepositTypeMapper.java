package com.cashsystem.mapper;

import com.cashsystem.entity.DepositType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepositTypeMapper {

    @Select("SELECT * FROM deposit_types WHERE id = #{id}")
    DepositType findById(Long id);

    @Select("SELECT * FROM deposit_types WHERE client_id = #{clientId} AND status = '启用'")
    List<DepositType> findByClientId(Long clientId);

    @Select("SELECT * FROM deposit_types WHERE type_code = #{typeCode} AND client_id = #{clientId}")
    DepositType findByTypeCode(String typeCode, Long clientId);

    @Insert("INSERT INTO deposit_types(client_id, type_code, type_name, description, min_amount, max_amount, currency, status) " +
            "VALUES(#{clientId}, #{typeCode}, #{typeName}, #{description}, #{minAmount}, #{maxAmount}, #{currency}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DepositType depositType);

    @Update("UPDATE deposit_types SET type_name=#{typeName}, description=#{description}, min_amount=#{minAmount}, max_amount=#{maxAmount}, status=#{status} WHERE id=#{id}")
    int update(DepositType depositType);
}
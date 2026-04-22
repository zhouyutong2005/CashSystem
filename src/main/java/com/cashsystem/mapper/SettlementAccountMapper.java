package com.cashsystem.mapper;

import com.cashsystem.entity.SettlementAccount;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SettlementAccountMapper {

    @Select("SELECT * FROM settlement_accounts WHERE id = #{id}")
    SettlementAccount findById(Long id);

    @Select("SELECT * FROM settlement_accounts WHERE client_id = #{clientId} AND is_default = true")
    SettlementAccount findDefaultByClientId(Long clientId);

    @Select("SELECT * FROM settlement_accounts WHERE client_id = #{clientId}")
    List<SettlementAccount> findByClientId(Long clientId);

    @Insert("INSERT INTO settlement_accounts(client_id, account_name, bank_name, account_number, bank_code, account_type, balance, is_default) " +
            "VALUES(#{clientId}, #{accountName}, #{bankName}, #{accountNumber}, #{bankCode}, #{accountType}, #{balance}, #{isDefault})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SettlementAccount account);

    @Update("UPDATE settlement_accounts SET balance = #{balance} WHERE id = #{id}")
    int updateBalance(Long id, java.math.BigDecimal balance);

    @Update("UPDATE settlement_accounts SET is_default = false WHERE client_id = #{clientId}")
    int clearDefaultFlag(Long clientId);
}
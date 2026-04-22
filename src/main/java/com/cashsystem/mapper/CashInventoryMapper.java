package com.cashsystem.mapper;

import com.cashsystem.entity.CashInventory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CashInventoryMapper {

    @Select("SELECT * FROM cash_inventory WHERE device_id = #{deviceId} AND currency = #{currency}")
    List<CashInventory> findByDeviceAndCurrency(Long deviceId, String currency);

    @Select("SELECT * FROM cash_inventory WHERE device_id = #{deviceId}")
    List<CashInventory> findByDeviceId(Long deviceId);

    @Insert("INSERT INTO cash_inventory(device_id, currency, denomination, quantity, total_amount, bag_number, updated_time) " +
            "VALUES(#{deviceId}, #{currency}, #{denomination}, #{quantity}, #{totalAmount}, #{bagNumber}, #{updatedTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CashInventory inventory);

    @Update("UPDATE cash_inventory SET quantity = #{quantity}, total_amount = #{totalAmount}, updated_time = #{updatedTime} WHERE id = #{id}")
    int update(CashInventory inventory);

    @Delete("DELETE FROM cash_inventory WHERE device_id = #{deviceId}")
    int deleteByDeviceId(Long deviceId);
}
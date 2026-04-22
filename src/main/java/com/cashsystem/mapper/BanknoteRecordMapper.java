package com.cashsystem.mapper;

import com.cashsystem.entity.BanknoteRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BanknoteRecordMapper {

    @Select("SELECT * FROM banknote_records WHERE id = #{id}")
    BanknoteRecord findById(Long id);

    @Select("SELECT * FROM banknote_records WHERE deposit_record_id = #{depositRecordId}")
    List<BanknoteRecord> findByDepositRecordId(Long depositRecordId);

    @Select("SELECT * FROM banknote_records WHERE serial_number = #{serialNumber}")
    BanknoteRecord findBySerialNumber(String serialNumber);

    @Insert("INSERT INTO banknote_records(deposit_record_id, serial_number, currency, denomination, record_time) " +
            "VALUES(#{depositRecordId}, #{serialNumber}, #{currency}, #{denomination}, #{recordTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BanknoteRecord record);

    int batchInsert(List<BanknoteRecord> records);

    List<BanknoteRecord> findByDepositRecordIds(@Param("depositRecordIds") List<Long> depositRecordIds);

    Integer countByDepositRecordId(Long depositRecordId);
}
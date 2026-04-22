package com.cashsystem.mapper;

import com.cashsystem.entity.CleaningRecord;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CleaningRecordMapper {

    @Select("SELECT * FROM cleaning_records WHERE id = #{id}")
    CleaningRecord findById(Long id);

    @Select("SELECT * FROM cleaning_records WHERE device_id = #{deviceId} AND clearing_date = #{date}")
    CleaningRecord findByDeviceAndDate(Long deviceId, LocalDate date);

    @Select("SELECT * FROM cleaning_records WHERE operator_id = #{operatorId} AND clearing_date BETWEEN #{startDate} AND #{endDate}")
    List<CleaningRecord> findByOperatorAndDateRange(Long operatorId, LocalDate startDate, LocalDate endDate);

    @Insert("INSERT INTO cleaning_records(device_id, old_bag_number, new_bag_number, old_batch_number, new_batch_number, total_amount, storage_status, clearing_date, operator_id) " +
            "VALUES(#{deviceId}, #{oldBagNumber}, #{newBagNumber}, #{oldBatchNumber}, #{newBatchNumber}, #{totalAmount}, #{storageStatus}, #{clearingDate}, #{operatorId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CleaningRecord record);

    @Update("UPDATE cleaning_records SET storage_status = #{storageStatus} WHERE id = #{id}")
    int updateStorageStatus(Long id, String storageStatus);
}
package com.cashsystem.mapper;

import com.cashsystem.entity.DailySettlement;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DailySettlementMapper {

    @Select("SELECT * FROM daily_settlements WHERE id = #{id}")
    DailySettlement findById(Long id);

    @Select("SELECT * FROM daily_settlements WHERE client_id = #{clientId} AND settlement_date = #{date}")
    DailySettlement findByClientAndDate(Long clientId, LocalDate date);

    @Select("SELECT * FROM daily_settlements WHERE client_id = #{clientId} AND settlement_date BETWEEN #{startDate} AND #{endDate}")
    List<DailySettlement> findByClientAndDateRange(Long clientId, LocalDate startDate, LocalDate endDate);

    @Insert("INSERT INTO daily_settlements(client_id, total, adjustment, type, status, batch_number, settlement_date) " +
            "VALUES(#{clientId}, #{total}, #{adjustment}, #{type}, #{status}, #{batchNumber}, #{settlementDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DailySettlement settlement);

    @Update("UPDATE daily_settlements SET status = #{status}, audit_time = NOW() WHERE id = #{id}")
    int updateStatus(Long id, String status);
}
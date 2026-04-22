package com.cashsystem.mapper;

import com.cashsystem.entity.BlockchainRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlockchainRecordMapper {

    @Insert("INSERT INTO blockchain_records(business_type, business_id, data_hash, tx_hash, block_hash, block_number, status, chain_type, create_time, update_time, remark) " +
            "VALUES(#{businessType}, #{businessId}, #{dataHash}, #{txHash}, #{blockHash}, #{blockNumber}, #{status}, #{chainType}, #{createTime}, #{updateTime}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BlockchainRecord record);

    @Select("SELECT * FROM blockchain_records WHERE id = #{id}")
    BlockchainRecord findById(Long id);

    @Select("SELECT * FROM blockchain_records WHERE business_type = #{businessType} AND business_id = #{businessId}")
    BlockchainRecord findByBusinessRecord(@Param("businessType") String businessType, @Param("businessId") Long businessId);

    @Select("SELECT * FROM blockchain_records WHERE tx_hash = #{txHash}")
    BlockchainRecord findByTxHash(String txHash);

    @Select("SELECT * FROM blockchain_records WHERE status = #{status} ORDER BY create_time DESC")
    List<BlockchainRecord> findByStatus(String status);

    @Update("UPDATE blockchain_records SET tx_hash=#{txHash}, block_hash=#{blockHash}, block_number=#{blockNumber}, status=#{status}, update_time=#{updateTime} WHERE id=#{id}")
    int updateBlockchainInfo(BlockchainRecord record);

    @Update("UPDATE blockchain_records SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Select("SELECT COUNT(*) FROM blockchain_records WHERE business_type = #{businessType} AND status = 'SUCCESS'")
    int countSuccessRecords(String businessType);
}
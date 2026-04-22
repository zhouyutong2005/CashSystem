package com.cashsystem.mapper;

import com.cashsystem.entity.Client;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClientMapper {

    @Select("SELECT * FROM clients WHERE id = #{id}")
    Client findById(Long id);

    @Select("SELECT * FROM clients WHERE status = '启用'")
    List<Client> findAllActive();

    @Insert("INSERT INTO clients(client_name, contact_person, contact_phone, address, status) " +
            "VALUES(#{clientName}, #{contactPerson}, #{contactPhone}, #{address}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Client client);

    @Update("UPDATE clients SET client_name=#{clientName}, contact_person=#{contactPerson}, " +
            "contact_phone=#{contactPhone}, address=#{address}, status=#{status} WHERE id=#{id}")
    int update(Client client);

    @Update("UPDATE clients SET status = '停用' WHERE id = #{id}")
    int disable(Long id);
}
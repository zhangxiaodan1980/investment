package com.gov.investment.mapper;

import com.gov.investment.model.Supplier;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SupplierMapper {

    @Insert("INSERT INTO supplier (name, contact_person, contact_phone, email, address, credit_code, remark, status, create_time, update_time) " +
            "VALUES (#{name}, #{contactPerson}, #{contactPhone}, #{email}, #{address}, #{creditCode}, #{remark}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Supplier supplier);

    @Update("UPDATE supplier SET name = #{name}, contact_person = #{contactPerson}, contact_phone = #{contactPhone}, email = #{email}, " +
            "address = #{address}, credit_code = #{creditCode}, remark = #{remark}, status = #{status}, update_time = #{updateTime} WHERE id = #{id}")
    void update(Supplier supplier);

    @Delete("DELETE FROM supplier WHERE id = #{id}")
    void delete(BigDecimal id);

    @Select("SELECT * FROM supplier WHERE id = #{id}")
    Supplier findById(BigDecimal id);

    @Select("SELECT * FROM supplier WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<Supplier> findByName(String name);

    @Select("SELECT * FROM supplier")
    List<Supplier> findAll();

    @Select("SELECT COUNT(*) FROM supplier WHERE contact_phone = #{contactPhone}")
    int countByContactPhone(String contactPhone);

    @Select("SELECT COUNT(*) FROM supplier WHERE credit_code = #{creditCode}")
    int countByCreditCode(String creditCode);
}
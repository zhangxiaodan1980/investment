package com.gov.investment.mapper;

import com.gov.investment.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInfoMapper {

    UserInfo selectUserByUsername(@Param("username") String username);

    void insert(UserInfo userInfo);
}

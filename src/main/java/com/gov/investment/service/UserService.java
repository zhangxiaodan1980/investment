package com.gov.investment.service;

import com.gov.investment.model.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {

    UserInfo getUserByUsername(String username);

    void register(UserInfo userInfo);
}

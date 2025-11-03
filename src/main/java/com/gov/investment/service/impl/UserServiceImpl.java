package com.gov.investment.service.impl;

import com.gov.investment.model.UserInfo;
import com.gov.investment.mapper.UserInfoMapper;
import com.gov.investment.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserInfo getUserByUsername(String username) {
        return userInfoMapper.selectUserByUsername(username);
    }

    public void register(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        if (userInfo.getEnabled() == null) {
            userInfo.setEnabled(true);
        }
        userInfoMapper.insert(userInfo);
    }
}

package com.gov.investment.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserInfo {

    private String username;

    private BigDecimal id;

    private String email;

    private String password;

    private String roleName;

    private Boolean enabled;
}

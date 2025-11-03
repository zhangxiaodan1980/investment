package com.gov.investment.controller;

import com.gov.investment.model.LoginRequest;
import com.gov.investment.model.UserInfo;
import com.gov.investment.service.UserService;
import com.gov.investment.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/busi")
@Slf4j
public class BusinessController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/info")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getInfo(){
        return ResponseEntity.ok("OK");
    }
}

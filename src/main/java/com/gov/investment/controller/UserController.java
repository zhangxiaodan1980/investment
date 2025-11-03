package com.gov.investment.controller;

import com.gov.investment.model.LoginRequest;
import com.gov.investment.model.UserInfo;
import com.gov.investment.service.UserService;
import com.gov.investment.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        UserInfo userInfo = userService.getUserByUsername(loginRequest.getUsername());
        if (userInfo != null && userInfo.getEnabled()) {
            // 实际项目中这里应该验证密码
            String token = jwtUtils.generateToken(userInfo.getUsername(), "ADMIN");

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", userInfo.getUsername());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserInfo userInfo) {
        try {
            userService.register(userInfo);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Registration failed");
        }
    }
}

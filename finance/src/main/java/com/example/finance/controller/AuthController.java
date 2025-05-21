package com.example.finance.controller;

import com.example.finance.model.AuthRequest;
import com.example.finance.model.AuthResponse;
import com.example.finance.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// ✅ เพิ่ม CORS ตรงนี้
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ✅ Signup
    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody AuthRequest request) {
        return authService.signup(request);
    }

    // ✅ Login
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }
}


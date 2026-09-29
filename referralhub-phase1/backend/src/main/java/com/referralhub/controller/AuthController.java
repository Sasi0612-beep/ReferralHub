package com.referralhub.controller;

import com.referralhub.dto.*;
import com.referralhub.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service) { this.service = service; }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }
}

package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.LoginRequest;
import com.workintech.twitterapi.dto.LoginResponse;
import com.workintech.twitterapi.dto.RegisterRequest;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}

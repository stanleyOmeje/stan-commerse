package com.stan.stancommerce.controller;

import com.stan.stancommerce.dto.LoginRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.service.security.authservice.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<DefaultResponse> loginUser(@RequestBody LoginRequest loginRequest){
        DefaultResponse<?> response = new DefaultResponse<>();
        response = authService.loginUser(loginRequest);
        return ResponseEntity.ok().body(response);
    }
}

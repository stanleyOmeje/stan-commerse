package com.stan.stancommerce.controller;

import com.stan.stancommerce.dto.LoginRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.service.UserService;
import com.stan.stancommerce.service.security.authservice.AuthService;
import com.stan.stancommerce.service.security.authservice.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<DefaultResponse> loginUser(@RequestBody LoginRequest loginRequest){
        DefaultResponse<?> response = new DefaultResponse<>();
        response = authService.loginUser(loginRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<DefaultResponse<?>> validateToken(@RequestHeader("Authorization") String authToken) throws JwtException {
        log.info("in validateToken with authToken {}", authToken);
        log.info("Testing filter");
        DefaultResponse<?> response = new DefaultResponse<>();
        String token = authToken.substring(7);
        boolean isTokenValid = jwtService.validatoken(token);
        if (isTokenValid) {
            response.setStatus("00");
            response.setMessage("Token is valid");
            return ResponseEntity.ok().body(response);
        }
        response.setStatus("01");
        response.setMessage("Token is not valid");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/login-user")
    public ResponseEntity<DefaultResponse<?>> getCurrentUser(@RequestHeader("Authorization") String authToken) throws JwtException {
        log.info("in getCurrentUser with authToken {}", authToken);
        DefaultResponse<?> response = new DefaultResponse<>();
        String token = authToken.substring(7);
        response = userService.getLoginUser(token);
        return ResponseEntity.ok().body(response);
    }
}

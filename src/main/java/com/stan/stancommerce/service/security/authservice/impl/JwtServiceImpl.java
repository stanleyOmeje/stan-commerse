package com.stan.stancommerce.service.security.authservice.impl;

import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.service.security.authservice.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${spring.jwt.key}")
    String key;
    long expirationTime =86400000;
    public String generateToken(String email) {
        String jwts = Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(getKey(key))
            .compact();
        return jwts;
    }

    public SecretKey getKey(String key) {
        byte[] bytes = key.getBytes();
        return Keys.hmacShaKeyFor(bytes);
    }
}

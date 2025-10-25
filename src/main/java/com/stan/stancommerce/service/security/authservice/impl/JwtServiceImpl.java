package com.stan.stancommerce.service.security.authservice.impl;

import com.stan.stancommerce.entities.User;
import com.stan.stancommerce.service.security.authservice.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {
    @Value("${spring.jwt.key}")
    String key;
    long expirationTime = 86400000;
    long refreshTokenExpirationTime = 86400000;

    public String generateToken(User user) {
        log.info("Generating JWT token for id: ...{} and key ...{}", user.getId(), this.key);
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        String jwts = Jwts.builder()
            .setClaims(claims)
            .setSubject(user.getId().toString())
            .setIssuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(getKey(key))
            .compact();
        return jwts;
    }

    public String generateRefeshToken(User user) {
        log.info("Generating JWT token for id: ...{} and key ...{}", user.getId(), this.key);
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        String jwts = Jwts.builder()
            .setClaims(claims)
            .setSubject(user.getId().toString())
            .setIssuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
            .signWith(getKey(key))
            .compact();
        return jwts;
    }

    public SecretKey getKey(String key) {
        byte[] bytes = key.getBytes();
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean validatoken(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(getKey(key))
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}

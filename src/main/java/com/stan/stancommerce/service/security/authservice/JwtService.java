package com.stan.stancommerce.service.security.authservice;

public interface JwtService {
    public String generateToken(String email);
}

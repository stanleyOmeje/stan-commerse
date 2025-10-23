package com.stan.stancommerce.service.security.authservice;

import com.stan.stancommerce.entities.User;

public interface JwtService {
    public String generateToken(User user);
    public boolean validatoken(String token);

    String getSubject(String token);
    String generateRefeshToken(User user);
}

package com.stan.stancommerce.service.security.authservice;

import com.stan.stancommerce.entities.User;

public interface JwtService {
    String generateToken(User user);

    boolean validatoken(String token);

    String getSubject(String token);

    String generateRefeshToken(User user);
}

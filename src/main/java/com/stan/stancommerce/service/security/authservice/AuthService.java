package com.stan.stancommerce.service.security.authservice;

import com.stan.stancommerce.dto.LoginRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;

public interface AuthService {
    DefaultResponse<?> loginUser(LoginRequest loginRequest);

}

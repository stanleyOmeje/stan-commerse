package com.stan.stancommerce.service.security.authservice;

import com.stan.stancommerce.dto.LoginRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.entities.User;

public interface AuthService {
    DefaultResponse<?> loginUser(LoginRequest loginRequest);

    User getLoggedInUser();

}

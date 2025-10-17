package com.stan.stancommerce.service;

import com.stan.stancommerce.dto.*;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.entities.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAll(String name);

    UserDto findById(long id);

    UserDto updateUser(UpdateUserRequest request, long id);

    UserDto changePassword(ChangePasswordRequest request, long id);

    UserDto registerUser(RegisterUserRequest request);

    DefaultResponse<?> loginUser(LoginRequest loginRequest);
}

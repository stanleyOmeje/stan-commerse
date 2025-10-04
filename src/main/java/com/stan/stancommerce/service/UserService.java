package com.stan.stancommerce.service;

import com.stan.stancommerce.dto.UserDto;
import com.stan.stancommerce.entities.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAll(String name);

    UserDto findById(long id);
}

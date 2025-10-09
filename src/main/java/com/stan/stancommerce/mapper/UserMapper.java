package com.stan.stancommerce.mapper;

import com.stan.stancommerce.dto.RegisterUserRequest;
import com.stan.stancommerce.dto.UserDto;
import com.stan.stancommerce.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto UsertoUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public User mapRegisterUserRequestToUser(RegisterUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }
}

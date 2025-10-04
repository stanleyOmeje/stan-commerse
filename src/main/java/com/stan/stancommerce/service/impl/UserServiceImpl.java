package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.UserDto;
import com.stan.stancommerce.entities.User;
import com.stan.stancommerce.mapper.UserMapper;
import com.stan.stancommerce.repositories.UserRepository;
import com.stan.stancommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAll(String name) {
        Pageable pageable = PageRequest.of(0, 10);
       List<User> users = userRepository.findAll(Sort.by(name));
        return users.stream().map(userMapper::UsertoUserDto).toList();
    }

    @Override
    public UserDto findById(long id) {
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            return userMapper.UsertoUserDto(user);
        }
        return null;
    }
}

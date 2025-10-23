package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.*;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.entities.User;
import com.stan.stancommerce.enums.ResponseStatus;
import com.stan.stancommerce.enums.Roles;
import com.stan.stancommerce.exception.NotFoundException;
import com.stan.stancommerce.mapper.UserMapper;
import com.stan.stancommerce.repositories.UserRepository;
import com.stan.stancommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public UserDto updateUser(UpdateUserRequest request, long id) {
        log.info("Inside updateUser with id: " + id);
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
          throw new IllegalArgumentException("User not found");
        }
        User user = optionalUser.get();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return userMapper.UsertoUserDto(user);
    }

    @Override
    public UserDto changePassword(ChangePasswordRequest request, long id) {
        log.info("Inside changePassword with id: " + id);
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new IllegalArgumentException("User not found");
        }
        User user = optionalUser.get();
        if(!request.getOldPassword().equals(user.getPassword())){
            throw new IllegalArgumentException("Old password does not match");
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return userMapper.UsertoUserDto(user);
    }

    @Override
    public UserDto registerUser(RegisterUserRequest request) {
        UserDto userDto = null;
        log.info("Inside registerUser with request: " + request);
        String email = request.getEmail().replaceAll("\\s","");
        email = email.toLowerCase();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
          throw new IllegalArgumentException("User already in use");
        }
        User user = userMapper.mapRegisterUserRequestToUser(request);
        user.setRole(Roles.USER);
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
             user = userRepository.save(user);
             userDto = userMapper.UsertoUserDto(user);
             userDto.setId(user.getId());
             return userDto;

        }catch(Exception e){
            log.error(e.getMessage());
        }
        return userDto;
    }

    @Override
    public DefaultResponse<?> loginUser(LoginRequest loginRequest) {
        DefaultResponse<?> response = new DefaultResponse<>();
        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setMessage("registered unsuccessful");
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if(user.isEmpty()){
            throw new NotFoundException("User not found");
        }
        boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword());
        if(matches){
            response.setStatus(ResponseStatus.SUCCESS.getCode());
            response.setMessage("User registered successfully");
        }
        return response;
    }

    @Override
    public DefaultResponse<?>  getLoginUser(String token) {
        DefaultResponse<UserDto> response = new DefaultResponse<>();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String)authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new NotFoundException("User not found");
        }
        User user = optionalUser.get();
        UserDto userDto = userMapper.UsertoUserDto(user);
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage("User found successfully");
        response.setData(userDto);
        return response;
    }
}

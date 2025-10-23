package com.stan.stancommerce.service.security.authservice.impl;

import com.stan.stancommerce.dto.LoginRequest;
import com.stan.stancommerce.dto.jwt.JwtResponse;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.entities.User;
import com.stan.stancommerce.enums.ResponseStatus;
import com.stan.stancommerce.repositories.UserRepository;
import com.stan.stancommerce.service.security.authservice.AuthService;
import com.stan.stancommerce.service.security.authservice.JwtService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Override
    public DefaultResponse<?> loginUser(LoginRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        DefaultResponse<JwtResponse> response = new DefaultResponse<>();
        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setMessage(ResponseStatus.FAILED.getMessage());
        try{
            String token = null;
            String refrshToken = null;
            if (StringUtils.isNotBlank(loginRequest.getEmail()) && StringUtils.isNotBlank(loginRequest.getPassword())) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
                User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
                if (user != null) {
                    token = jwtService.generateToken(user);
                    refrshToken = jwtService.generateRefeshToken(user);
                    jwtResponse.setToken(token);
                    jwtResponse.setRefreshToken(refrshToken);
                }
                response.setStatus(ResponseStatus.SUCCESS.getCode());
                response.setMessage(ResponseStatus.SUCCESS.getMessage());
                response.setData(jwtResponse);
                return response;
            }
        }catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(ResponseStatus.BAD_REQUEST.getCode());
            response.setMessage(ResponseStatus.BAD_REQUEST.getMessage());
            return response;
        }
        return response;
    }
}

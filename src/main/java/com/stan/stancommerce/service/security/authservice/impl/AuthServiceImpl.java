package com.stan.stancommerce.service.security.authservice.impl;

import com.stan.stancommerce.dto.LoginRequest;
import com.stan.stancommerce.dto.jwt.JwtResponse;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.entities.User;
import com.stan.stancommerce.enums.ResponseStatus;
import com.stan.stancommerce.exception.NotFoundException;
import com.stan.stancommerce.repositories.UserRepository;
import com.stan.stancommerce.service.security.authservice.AuthService;
import com.stan.stancommerce.service.security.authservice.JwtService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public DefaultResponse<?> loginUser(LoginRequest loginRequest, HttpServletResponse servletResponse) {
        JwtResponse jwtResponse = new JwtResponse();
        DefaultResponse<JwtResponse> response = new DefaultResponse<>();
        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setMessage(ResponseStatus.FAILED.getMessage());
        try {
            String token = null;
            String refrshToken = null;
            if (StringUtils.isNotBlank(loginRequest.getEmail()) && StringUtils.isNotBlank(loginRequest.getPassword())) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
                User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
                if (user != null) {
                    token = jwtService.generateToken(user);
                    refrshToken = jwtService.generateRefeshToken(user);

                    var cookie = new Cookie("refreshToken", refrshToken);
                    cookie.setPath("/auth/refresh-token");
                    cookie.setHttpOnly(true);
                    cookie.setMaxAge(604800);
                    cookie.setSecure(true);

                    servletResponse.addCookie(cookie);
                    jwtResponse.setToken(token);
                    jwtResponse.setRefreshToken(refrshToken);
                }
                response.setStatus(ResponseStatus.SUCCESS.getCode());
                response.setMessage(ResponseStatus.SUCCESS.getMessage());
                response.setData(jwtResponse);
                return response;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(ResponseStatus.BAD_REQUEST.getCode());
            response.setMessage(ResponseStatus.BAD_REQUEST.getMessage());
            return response;
        }
        return response;
    }

    @Override
    public DefaultResponse<?> refreshToken(String refeshToken) {
        log.info("Inside refreshToken method with refeshToken: {}", refeshToken);
        JwtResponse jwtResponse = new JwtResponse();
        DefaultResponse<JwtResponse> response = new DefaultResponse<>();
        if (!jwtService.validatoken(refeshToken)){
            response.setStatus(ResponseStatus.UNAUTHORIZED.getCode());
            response.setMessage(ResponseStatus.UNAUTHORIZED.getMessage());
            return response;
        }
        String id = jwtService.getSubject(refeshToken);
        String token = null;
        User user = userRepository.findById(Long.parseLong(id)).orElse(null);
        if (user != null) {
           token = jwtService.generateToken(user);
           jwtResponse.setToken(token);
        }
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(jwtResponse);
        log.info("response: {}", response);
        return response;
    }

    @Override
    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getMessage());
        }
        String email = auth.getPrincipal().toString();
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
        if (user == null) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getMessage());
        }
        return user;
    }


}

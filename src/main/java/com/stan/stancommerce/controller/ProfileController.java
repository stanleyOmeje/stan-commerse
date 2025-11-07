package com.stan.stancommerce.controller;


import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.dto.response.UserProfileResponse;
import com.stan.stancommerce.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<DefaultResponse<?>> getUserProfile() {
        DefaultResponse<?> response = new DefaultResponse<>();
        response= profileService.getUserProfile();
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(response);
    }
}

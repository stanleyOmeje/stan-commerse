package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.dto.response.UserProfileResponse;
import com.stan.stancommerce.entities.Profile;
import com.stan.stancommerce.entities.User;
import com.stan.stancommerce.enums.ResponseStatus;
import com.stan.stancommerce.exception.NotFoundException;
import com.stan.stancommerce.mapper.UserMapper;
import com.stan.stancommerce.repositories.ProfileRepository;
import com.stan.stancommerce.service.ProfileService;
import com.stan.stancommerce.service.security.authservice.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final AuthService authService;
    private final ProfileRepository profileRepository;
    private final UserMapper userMapper;


    public DefaultResponse<UserProfileResponse> getUserProfile() {
        log.debug("Inside getUserProfile");
        DefaultResponse<UserProfileResponse> response = new DefaultResponse<>();

        try {
            User user = authService.getLoggedInUser();
            Profile profile = null;
            if (user == null) {
                throw new NotFoundException("User not found");
            }
            try {
                profile = profileRepository.findByUser(user)
                    .orElseThrow(() -> new NotFoundException("Profile not found"));
            } catch (Exception e) {
                profile = null;
            }
//            Profile profile = profileRepository.findByUser(user)
//                .orElseThrow(() -> new NotFoundException("Profile not found"));

            UserProfileResponse userProfileResponse = userMapper.mapUserToUserProfileResponse(user, profile);

            response.setStatus(ResponseStatus.SUCCESS.getCode());
            response.setMessage(ResponseStatus.SUCCESS.getMessage());
            response.setData(userProfileResponse);

            log.info("User profile fetched successfully: {}", userProfileResponse);
            return response;

        } catch (NotFoundException e) {
            log.warn("NotFoundException in getUserProfile: {}", e.getMessage());
            response.setStatus(ResponseStatus.NOT_FOUND.getCode());
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error in getUserProfile: {}", e.getMessage(), e);
            response.setStatus(ResponseStatus.FAILED.getCode());
            response.setMessage("An unexpected error occurred while fetching user profile");
        }

        return response;
    }

}

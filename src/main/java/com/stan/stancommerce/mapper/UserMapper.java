package com.stan.stancommerce.mapper;

import com.stan.stancommerce.dto.RegisterUserRequest;
import com.stan.stancommerce.dto.UserDto;
import com.stan.stancommerce.dto.response.ProfileDto;
import com.stan.stancommerce.dto.response.UserProfileResponse;
import com.stan.stancommerce.entities.Profile;
import com.stan.stancommerce.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto UsertoUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().toString());
        userDto.setId(user.getId());
        return userDto;
    }

    public User mapRegisterUserRequestToUser(RegisterUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    public UserProfileResponse mapUserToUserProfileResponse(User user, Profile profile) {
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        UserDto userDto = null;
        ProfileDto profileDto = null;
        if(user != null) {
            userDto = UsertoUserDto(user);
        }
        if(profile != null) {
            profileDto = new ProfileDto();
            profileDto.setBio(profile.getBio());
            profileDto.setLoyaltyPoints(profile.getLoyaltyPoints());
            profileDto.setDateOfBirth(profile.getDateOfBirth());
            profileDto.setLoyaltyPoints(profile.getLoyaltyPoints());
            profileDto.setPhoneNumber(profile.getPhoneNumber());
        }
        userProfileResponse.setUser(userDto);
        userProfileResponse.setProfile(profileDto);
        return userProfileResponse;
    }
}

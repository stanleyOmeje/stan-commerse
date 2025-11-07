package com.stan.stancommerce.dto.response;

import com.stan.stancommerce.dto.UserDto;
import lombok.Data;

@Data
public class UserProfileResponse {
    UserDto user;
    ProfileDto profile;
}

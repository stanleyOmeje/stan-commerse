package com.stan.stancommerce.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;
}

package com.stan.stancommerce.dto;

import com.stan.stancommerce.annotation.LowercaseAnnotation;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "name required")
    private String name;
    @LowercaseAnnotation(message = "Email must be in lowercase")
    @Email(message = "Email required")
    private String email;
    @Min(value = 5, message = "password must be greater than 4 characters")
    private String password;
}

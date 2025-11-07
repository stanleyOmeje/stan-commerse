package com.stan.stancommerce.dto.response;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileDto {
    private String bio;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Integer loyaltyPoints;
}

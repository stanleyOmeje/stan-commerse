package com.stan.stancommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartRequest {
    @NotNull
    private Integer quantity;
}

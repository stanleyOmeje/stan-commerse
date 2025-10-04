package com.stan.stancommerce.dto;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    @Column(nullable = false)
    private String categoryId;
}

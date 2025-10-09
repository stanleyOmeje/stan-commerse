package com.stan.stancommerce.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne
    @JoinColumn
    private Cart cart;

    @ManyToOne
    @JoinColumn
    private Product product;

    public BigDecimal getTotalPrice() {
        return BigDecimal.valueOf(quantity).multiply(product.getPrice());
    }
}

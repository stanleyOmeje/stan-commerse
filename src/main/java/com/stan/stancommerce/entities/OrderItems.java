package com.stan.stancommerce.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private Integer quantity;

    @ManyToOne
    @JoinColumn
    private Orders orders;

    @ManyToOne
    @JoinColumn
    private Product product;
}

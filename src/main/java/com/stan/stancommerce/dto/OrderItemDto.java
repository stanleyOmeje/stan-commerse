package com.stan.stancommerce.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private OrderProductDto product;
    private long quantity;
    private BigDecimal totalPrice;
}

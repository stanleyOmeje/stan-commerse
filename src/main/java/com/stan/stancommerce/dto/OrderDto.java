package com.stan.stancommerce.dto;

import com.stan.stancommerce.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    List<OrderItemDto> items = new ArrayList<>();
    private BigDecimal totalPrice;
}

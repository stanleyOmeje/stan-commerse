package com.stan.stancommerce.dto.response;

import lombok.Data;

@Data
public class CheckoutResponse {
    private Long orderId;
    private String checkUrl;
}

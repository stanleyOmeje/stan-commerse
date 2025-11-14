package com.stan.stancommerce.dto.response;

import com.stan.stancommerce.enums.OrderStatus;
import lombok.Data;

@Data
public class WebhookPaymentResponse {
    private Long orderId;
    private String orderStatus;
}

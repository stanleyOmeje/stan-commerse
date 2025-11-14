package com.stan.stancommerce.service;

import com.stan.stancommerce.dto.WebhookPaymentRequest;
import com.stan.stancommerce.dto.response.WebhookPaymentResponse;
import com.stan.stancommerce.entities.Orders;

public interface PaymentGateway {
    String pay(Orders orders);
    WebhookPaymentResponse paymentWebbook(WebhookPaymentRequest webhookPaymentRequest);
}

package com.stan.stancommerce.service;

import com.stan.stancommerce.dto.WebhookPaymentRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stripe.exception.SignatureVerificationException;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    DefaultResponse paymentWebhook(WebhookPaymentRequest webhookPaymentRequest) throws SignatureVerificationException;
}

package com.stan.stancommerce.controller;

import com.stan.stancommerce.dto.CheckoutRequest;
import com.stan.stancommerce.dto.WebhookPaymentRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.service.OrderService;
import com.stan.stancommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<DefaultResponse<?>> checkout(@RequestBody CheckoutRequest checkoutRequest) {
        log.info("Inside OrderController::checkout with Checkout request: {}", checkoutRequest);
        DefaultResponse<?> response = null;
        try {
            response = orderService.checkout(checkoutRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> paymentWebhook(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        log.info("Inside OrderController::paymentWebhook with headers...{}, request: {}",headers, payload);
        // http://localhost:9005/checkout/webhook
        try {
            WebhookPaymentRequest webhookPaymentRequest = new WebhookPaymentRequest();
            webhookPaymentRequest.setHeaders(headers);
            webhookPaymentRequest.setPayload(payload);
            log.info("WebhookPaymentRequest sent to payment webhook : {}", webhookPaymentRequest);
            return ResponseEntity.ok(paymentService.paymentWebhook(webhookPaymentRequest));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

}

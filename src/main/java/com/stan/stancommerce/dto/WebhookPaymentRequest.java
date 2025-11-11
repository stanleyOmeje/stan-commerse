package com.stan.stancommerce.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class WebhookPaymentRequest {
    private Map<String, String> headers = new HashMap<>();
    private String payload;

    public WebhookPaymentRequest() {
    }

    public WebhookPaymentRequest(Map<String, String> headers, String payload) {
        this.headers = headers;
        this.payload = payload;
    }
}

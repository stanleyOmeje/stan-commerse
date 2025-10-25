package com.stan.stancommerce.controller;

import com.stan.stancommerce.dto.CheckoutRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final OrderService orderService;

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

}

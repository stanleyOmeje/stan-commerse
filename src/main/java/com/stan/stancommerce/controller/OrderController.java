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
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<DefaultResponse<?>> getOrders() {
        log.info("Inside OrderController::getOrders");

        DefaultResponse<?> response = null;
        try {
            response = orderService.getOrders();
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultResponse<?>> getSingleOrder(@PathVariable Long id) {
        log.info("Inside OrderController::getSingleOrder");

        DefaultResponse<?> response = null;
        try {
            response = orderService.getSingleOrder(id);
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}

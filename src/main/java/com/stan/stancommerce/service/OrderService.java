package com.stan.stancommerce.service;

import com.stan.stancommerce.dto.CheckoutRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    DefaultResponse checkout(CheckoutRequest checkoutRequest);


    DefaultResponse<?> getOrders();

    DefaultResponse<?> getSingleOrder(Long id);
}

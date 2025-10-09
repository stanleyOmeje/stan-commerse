package com.stan.stancommerce.service;

import com.stan.stancommerce.dto.AddItemtoCartRequest;
import com.stan.stancommerce.dto.CartDto;
import com.stan.stancommerce.dto.CartItemDto;
import org.springframework.http.ResponseEntity;

public interface CartService {
    CartDto createCart();
    CartItemDto addToCart(Long productId, AddItemtoCartRequest request);


}

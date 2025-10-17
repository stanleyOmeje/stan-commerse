package com.stan.stancommerce.service;

import com.stan.stancommerce.dto.AddItemtoCartRequest;
import com.stan.stancommerce.dto.CartDto;
import com.stan.stancommerce.dto.CartItemDto;
import com.stan.stancommerce.dto.UpdateCartRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import org.springframework.http.ResponseEntity;

public interface CartService {
    DefaultResponse createCart();
    DefaultResponse addToCart(Long productId, AddItemtoCartRequest request);


    DefaultResponse getCart(Long cartId);

    DefaultResponse updateCartItems(Long cartId, Long productId, UpdateCartRequest updateCartRequest);

    void removeProductFromCart(Long cartId, Long productId);

    void clearCart(Long cartId);
}

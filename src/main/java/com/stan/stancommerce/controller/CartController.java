package com.stan.stancommerce.controller;

import com.stan.stancommerce.dto.AddItemtoCartRequest;
import com.stan.stancommerce.dto.CartDto;
import com.stan.stancommerce.dto.CartItemDto;
import com.stan.stancommerce.dto.UpdateCartRequest;
import com.stan.stancommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(){
        CartDto cartDto = null;
        try {
            cartDto = cartService.createCart();
            return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @PostMapping ("/{cartId}/item")
    public ResponseEntity<CartItemDto> addToCart(
        @PathVariable("cartId") Long id,
        @RequestBody AddItemtoCartRequest request
    ){
        CartItemDto addToCartDto = null;
        try {
            addToCartDto = cartService.addToCart(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(addToCartDto);
        }catch (Exception e){
            log.error(e.getMessage());
        }
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable Long cartId) {
        CartDto cartDto = null;
        try {
            cartDto = cartService.getCart(cartId);
            return ResponseEntity.status(HttpStatus.OK).body(cartDto);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateCartItems(@PathVariable("cartId") Long cartId,
                                                       @PathVariable("productId") Long productId,
                                                       @Valid @RequestBody UpdateCartRequest updateCartRequest) {
        CartItemDto cartItemDto = null;
        try {
            cartItemDto = cartService.updateCartItems(cartId,productId, updateCartRequest);
            return ResponseEntity.status(HttpStatus.OK).body(cartItemDto);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}

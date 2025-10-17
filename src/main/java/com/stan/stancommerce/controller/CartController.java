package com.stan.stancommerce.controller;

import com.stan.stancommerce.dto.AddItemtoCartRequest;
import com.stan.stancommerce.dto.CartDto;
import com.stan.stancommerce.dto.CartItemDto;
import com.stan.stancommerce.dto.UpdateCartRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<DefaultResponse<?>> createCart(){
        DefaultResponse<?> response = null;
        try {
            response = cartService.createCart();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }


    @PostMapping ("/{cartId}/item")
    public ResponseEntity<DefaultResponse<?>> addToCart(
        @PathVariable("cartId") Long id,
        @RequestBody AddItemtoCartRequest request
    ){
        DefaultResponse<?> response = null;
        CartItemDto addToCartDto = null;
        try {
            response = cartService.addToCart(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            log.error(e.getMessage());
        }
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @GetMapping("/{cartId}")
    public ResponseEntity<?> getCart(@PathVariable Long cartId) {
        DefaultResponse<?> response = null;
        try {
            response = cartService.getCart(cartId);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItems(@PathVariable("cartId") Long cartId,
                                          @PathVariable("productId") Long productId,
                                          @Valid @RequestBody UpdateCartRequest updateCartRequest) {
        DefaultResponse<?> response = null;
        CartItemDto cartItemDto = null;
        try {
            response = cartService.updateCartItems(cartId,productId, updateCartRequest);
            if (response == null) {
               return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/{cartId}/item/{productId}")
    public ResponseEntity<?> removeProductFromCart(@PathVariable("cartId") Long cartId,
                                                   @PathVariable("productId") Long productId){
        try {
            cartService.removeProductFromCart(cartId,productId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", "Product removed from cart"));
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", "Product not found"));
    }

    @DeleteMapping("/{cartId}/item")
    public ResponseEntity<?> clearCart(@PathVariable("cartId") Long cartId){
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", "Cart cleared"));
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", "Cart not found"));
    }

}

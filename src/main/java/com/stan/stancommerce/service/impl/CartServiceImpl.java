package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.AddItemtoCartRequest;
import com.stan.stancommerce.dto.CartDto;
import com.stan.stancommerce.dto.CartItemDto;
import com.stan.stancommerce.dto.UpdateCartRequest;
import com.stan.stancommerce.entities.Cart;
import com.stan.stancommerce.entities.CartItems;
import com.stan.stancommerce.entities.Product;
import com.stan.stancommerce.mapper.CartMapper;
import com.stan.stancommerce.repositories.CartRepository;
import com.stan.stancommerce.repositories.ProductRepository;
import com.stan.stancommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    @Override
    public CartDto createCart() {
        CartDto cartDto = null;
        try {
            Cart cart = new Cart();
            cart = cartRepository.save(cart);
            return cartMapper.mapCartToCartDto(cart);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public CartItemDto addToCart(Long cartId, AddItemtoCartRequest request) {
        //check if cart exit
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isEmpty()) {
            throw new IllegalArgumentException("Cart not found");
        }
        //check if product exit
        Optional<Product> product = productRepository.findById(request.getProductId());
        if (product.isEmpty()) {
            throw new IllegalArgumentException("product not found");
        }
        Cart cart = optionalCart.get();

        //check if product exist in CartItems
//        CartItems cartItems = cart.getCartItem(request.getProductId());
//        if (cartItems != null) {
//            cartItems.setQuantity(cartItems.getQuantity() + 1);
//        }else {
//            cartItems = new CartItems();
//            cartItems.setQuantity(1);
//            cartItems.setProduct(product.get());
//            cartItems.setCart(cart);
//            cart.getCartItems().add(cartItems);
//        }
        CartItems cartItems = cart.addCartItem(product.get());
        cart = cartRepository.save(cart);

        return cartMapper.mapCartToCartItemDto(cartItems);
    }

    @Override
    public CartDto getCart(Long cartId) {
        Cart cart = null;
            try {
                cart = cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
            }catch (Exception e) {
                log.error(e.getMessage());
            }
            Set<CartItems> cartItems = cart.getCartItems();
            if(cartItems != null && !cartItems.isEmpty()) {
                return cartMapper.mapCartItemtoCartDto(cart.getCartItems(),cart);
            }
            return null;
    }

    @Override
    public CartItemDto updateCartItems(Long cartId, Long productId, UpdateCartRequest updateCartRequest) {
        CartItemDto cartItemDto = null;
        Cart cart = null;
        try {
            cart = cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
            CartItems cartItems = cart.getCartItem(productId);
            if (cartItems != null) {
                cartItems.setQuantity(updateCartRequest.getQuantity());
                cart = cartRepository.save(cart);
                return cartMapper.mapCartToCartItemDto(cartItems);
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}

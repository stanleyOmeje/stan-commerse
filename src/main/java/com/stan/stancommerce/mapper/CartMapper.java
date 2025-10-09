package com.stan.stancommerce.mapper;

import com.stan.stancommerce.dto.CartDto;
import com.stan.stancommerce.dto.CartItemDto;
import com.stan.stancommerce.dto.CartProductDto;
import com.stan.stancommerce.entities.Cart;
import com.stan.stancommerce.entities.CartItems;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {
    public CartDto mapCartToCartDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        return cartDto;
    }

    public CartItemDto mapCartToCartItemDto(CartItems cartItems) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setQuantity(cartItems.getQuantity());
        cartItemDto.setTotalPrice(cartItems.getTotalPrice());

        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setId(cartItems.getProduct().getId());
        cartProductDto.setName(cartItems.getProduct().getName());
        cartProductDto.setPrice(cartItems.getProduct().getPrice());

        cartItemDto.setProduct(cartProductDto);

        return cartItemDto;
    }
}

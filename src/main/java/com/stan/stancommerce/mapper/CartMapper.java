package com.stan.stancommerce.mapper;

import com.stan.stancommerce.dto.CartDto;
import com.stan.stancommerce.dto.CartItemDto;
import com.stan.stancommerce.dto.CartProductDto;
import com.stan.stancommerce.entities.Cart;
import com.stan.stancommerce.entities.CartItems;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
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

    public CartDto mapCartItemtoCartDto(Set<CartItems> cartItems, Cart cart) {
        log.info("in mapCartItemtoCartDto with cartItems {} and Cart...{}", cartItems, cart);
        CartDto cartDto = new CartDto();
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        if (cartItems != null && !cartItems.isEmpty()) {
            cartItems.stream().map(cartItem -> {
                CartItemDto cartItemDto = new CartItemDto();

                cartItemDto.setQuantity(cartItem.getQuantity());
                cartItemDto.setTotalPrice(cartItem.getTotalPrice());

                CartProductDto cartProductDto = new CartProductDto();

                cartProductDto.setId(cartItem.getId());
                if(cartItem.getProduct() != null){
                    cartProductDto.setName(cartItem.getProduct().getName());
                    cartProductDto.setPrice(cartItem.getProduct().getPrice());
                }
                cartItemDto.setProduct(cartProductDto);
                cartItemDto.setProduct(cartProductDto);
                cartItemDtos.add(cartItemDto);
                return cartItemDto;
            }).toList();
        }
        cartDto.setItems(cartItemDtos);
        //cartDto.setId(cartItems.iterator().next().getId());
        cartDto.setTotalPrice(cart.getTotalPrice());
        return cartDto;
    }
}

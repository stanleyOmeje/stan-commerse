package com.stan.stancommerce.entities;

import com.stan.stancommerce.dto.CartItemDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateCreated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<CartItems> cartItems = new HashSet<>();

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        totalPrice = cartItems.stream().map(CartItems::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalPrice;
    }

    public CartItems getCartItem(Long productId) {
        CartItems cartItem = null;
        cartItem = cartItems.stream().filter(item -> item.getProduct().getId().equals(productId))
            .findFirst().orElse(null);
        return cartItem;
    }

    public CartItems addCartItem(Product product) {
        CartItems cartItem = getCartItem(product.getId());
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }else {
            cartItem = new CartItems();
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            cartItem.setProduct(product);
            cartItems.add(cartItem);
        }
        return cartItem;
    }

    public void removeCartItem(Long productId) {
        CartItems cartItem = getCartItem(productId);
        if (cartItem != null) {
            cartItems.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public void clearCart() {
        cartItems.clear();
    }
}



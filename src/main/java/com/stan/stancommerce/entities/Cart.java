package com.stan.stancommerce.entities;

import com.stan.stancommerce.dto.CartItemDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateCreated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE)
    private Set<CartItems> cartItems = new HashSet<>();

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        totalPrice = cartItems.stream().map(CartItems::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalPrice;
    }

    public CartItems getCartItem(Long productId) {
        return cartItems.stream().filter(item -> item.getProduct().getId().equals(productId))
            .findFirst().orElse(null);
    }

    public CartItems addCartItem(Product product) {
        CartItems cartItem = getCartItem(product.getId());
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }else {
            cartItem = new CartItems();
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            cartItem.setProduct(cartItem.getProduct());
            cartItems.add(cartItem);
        }
        return cartItem;
    }
}



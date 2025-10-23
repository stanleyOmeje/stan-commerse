package com.stan.stancommerce.mapper;

import com.stan.stancommerce.entities.Cart;
import com.stan.stancommerce.entities.OrderItems;
import com.stan.stancommerce.entities.Orders;
import com.stan.stancommerce.entities.Product;
import com.stan.stancommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderMapper {

    public Orders mapCartToOrder(Cart cart){
        Orders orders = new Orders();
        if(cart != null) {
            orders.setStatus(OrderStatus.PENDING);
            orders.setTotalPrice(cart.getTotalPrice());
            orders.setCreatedAt(LocalDateTime.now());
            List<OrderItems> items = new ArrayList<>();
            cart.getCartItems().stream().map(item->{
                OrderItems orderItem = new OrderItems();
                orderItem.setId(item.getId());
                orderItem.setCreatedAt(LocalDateTime.now());
                orderItem.setUnitPrice(item.getProduct().getPrice());
                orderItem.setTotalPrice(item.getTotalPrice());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setOrders(orders);
                orderItem.setProduct(item.getProduct());
                items.add(orderItem);
                return orderItem;
            }).toList();
        }
        return orders;
    }
}

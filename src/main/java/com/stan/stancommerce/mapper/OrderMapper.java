package com.stan.stancommerce.mapper;

import com.stan.stancommerce.dto.OrderDto;
import com.stan.stancommerce.dto.OrderItemDto;
import com.stan.stancommerce.dto.OrderProductDto;
import com.stan.stancommerce.entities.Cart;
import com.stan.stancommerce.entities.OrderItems;
import com.stan.stancommerce.entities.Orders;
import com.stan.stancommerce.entities.Product;
import com.stan.stancommerce.enums.OrderStatus;
import com.stan.stancommerce.service.security.authservice.AuthService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Component
public class OrderMapper {
    private final AuthService authService;

    public Orders mapCartToOrder(Cart cart){
        log.info("Mapping Cart to Order");
        Orders orders = new Orders();
        if(cart != null) {
            orders.setStatus(OrderStatus.PENDING);
            orders.setTotalPrice(cart.getTotalPrice());
            orders.setCreatedAt(LocalDateTime.now());
            orders.setCustomer(authService.getLoggedInUser());
            cart.getCartItems().stream().map(item->{
                OrderItems orderItem = new OrderItems();
                orderItem.setCreatedAt(LocalDateTime.now());
                orderItem.setUnitPrice(item.getProduct().getPrice());
                orderItem.setTotalPrice(item.getTotalPrice());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setOrders(orders);
                orderItem.setProduct(item.getProduct());
                orders.getItems().add(orderItem);
                return orderItem;
            }).toList();
        }
        log.info("orders...inside mapCartToOrder...{}", orders);
        return orders;
    }

    public OrderDto mapOrderToOrderDto(Orders order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setStatus(order.getStatus());
        orderDto.setCreatedAt(LocalDateTime.now());
        orderDto.setTotalPrice(order.getTotalPrice());

        order.getItems().stream().map(item->{
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setTotalPrice(item.getTotalPrice());
            orderItemDto.setQuantity(item.getQuantity());

            OrderProductDto product = new OrderProductDto();
            product.setId(item.getProduct().getId());
            product.setPrice(item.getProduct().getPrice());
            product.setName(item.getProduct().getName());

            orderItemDto.setProduct(product);
            orderDto.getItems().add(orderItemDto);
            return orderItemDto;
        }).toList();
        return orderDto;
    }
}

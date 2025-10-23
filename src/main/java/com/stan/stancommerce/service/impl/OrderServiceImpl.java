package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.CheckoutRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.entities.Cart;
import com.stan.stancommerce.entities.Orders;
import com.stan.stancommerce.entities.User;
import com.stan.stancommerce.enums.ResponseStatus;
import com.stan.stancommerce.exception.NotFoundException;
import com.stan.stancommerce.mapper.OrderMapper;
import com.stan.stancommerce.repositories.CartRepository;
import com.stan.stancommerce.repositories.OrderRepository;
import com.stan.stancommerce.repositories.UserRepository;
import com.stan.stancommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderService orderService;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    @Override
    public DefaultResponse<?> checkout(CheckoutRequest checkoutRequest) {
        log.debug("Inside OrderServiceImpl::checkout with Checkout request: {}", checkoutRequest);
        DefaultResponse<?> response = null;
        if (checkoutRequest != null) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getMessage());
        }
        Cart cart = cartRepository.findById(checkoutRequest.getCheckoutId()).orElseThrow(NotFoundException::new);
        if (cart.getCartItems().isEmpty()) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getMessage());
        }
        Orders order = orderMapper.mapCartToOrder(cart);
        //clear cart
        return null;
    }

    @Override
    public DefaultResponse<?> getOrders() {
        log.debug("Inside OrderServiceImpl::getOrders");

        DefaultResponse<List<Orders>> response = new DefaultResponse<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getMessage());
        }
        String email = auth.getPrincipal().toString();
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
        if (user == null) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getMessage());
        }

        List<Orders> orderDto = orderRepository.findByUser(user);
        if (orderDto.isEmpty()) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getMessage());
        }
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(orderDto);
        log.info("getOrders...{}", orderDto);
        return response;
    }

    @Override
    public DefaultResponse<?> getSingleOrder(Long id) {
        log.info("Inside OrderServiceImpl::getSingleOrder with id {}", id);
        DefaultResponse<?> response = null;
        Orders order = orderRepository.findById(id).orElseThrow(NotFoundException::new);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getMessage());
        }
        String email = auth.getPrincipal().toString();
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
        if (user == null) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getMessage());
        }
       //Check if order belongs to another user
        return null;
    }


}

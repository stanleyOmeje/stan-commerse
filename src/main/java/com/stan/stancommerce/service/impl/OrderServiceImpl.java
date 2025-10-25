package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.CheckoutRequest;
import com.stan.stancommerce.dto.OrderDto;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.entities.Cart;
import com.stan.stancommerce.entities.Orders;
import com.stan.stancommerce.entities.User;
import com.stan.stancommerce.enums.ResponseStatus;
import com.stan.stancommerce.exception.NotFoundException;
import com.stan.stancommerce.exception.UnAuthorizedException;
import com.stan.stancommerce.mapper.OrderMapper;
import com.stan.stancommerce.repositories.CartRepository;
import com.stan.stancommerce.repositories.OrderRepository;
import com.stan.stancommerce.repositories.UserRepository;
import com.stan.stancommerce.service.CartService;
import com.stan.stancommerce.service.OrderService;
import com.stan.stancommerce.service.security.authservice.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final AuthService authService;

    @Override
    public DefaultResponse<?> checkout(CheckoutRequest checkoutRequest) {
        log.debug("Inside OrderServiceImpl::checkout with Checkout request: {}", checkoutRequest);
        DefaultResponse<Long> response = new DefaultResponse<>();
        if (checkoutRequest == null) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getMessage());
        }
        Cart cart = null;
        try {
            cart = cartRepository.findById(checkoutRequest.getCheckoutId()).orElseThrow(NotFoundException::new);
        } catch (Exception e) {
            response = new DefaultResponse<>();
            response.setStatus(ResponseStatus.NOT_FOUND.getCode());
            response.setMessage(ResponseStatus.NOT_FOUND.getMessage());
            return response;
        }
        if (cart.getCartItems().isEmpty()) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getMessage());
        }
        Orders order = orderMapper.mapCartToOrder(cart);
        try {
            order = orderRepository.save(order);
            if (order.getId() != null) {
                response.setStatus(ResponseStatus.SUCCESS.getMessage());
                response.setMessage(ResponseStatus.SUCCESS.getMessage());
                response.setData(order.getId());
                cartService.clearCart(cart.getId());
                return response;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }

    @Override
    public DefaultResponse<?> getOrders() {
        log.debug("Inside OrderServiceImpl::getOrders");

        DefaultResponse<List<OrderDto>> response = new DefaultResponse<>();

        User user = authService.getLoggedInUser();

        List<Orders> orderList = orderRepository.findByCustomer(user);
        if (orderList.isEmpty()) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getMessage());
        }
        List<OrderDto> orderDtoList = orderList.stream().map(orderMapper::mapOrderToOrderDto).toList();
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(orderDtoList);
        log.info("getOrders...{}", orderDtoList);
        return response;
    }

    @Override
    public DefaultResponse<?> getSingleOrder(Long id) {
        log.info("Inside OrderServiceImpl::getSingleOrder with id {}", id);
        DefaultResponse<OrderDto> response = new DefaultResponse<>();
        Orders order = orderRepository.findById(id).orElseThrow(NotFoundException::new);
        User user = authService.getLoggedInUser();
        //Check if order belongs to another user
        if (!order.getCustomer().getId().equals(user.getId())) {
            throw new UnAuthorizedException(ResponseStatus.UNAUTHORIZED.getMessage());
        }
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(orderMapper.mapOrderToOrderDto(order));
        log.info("getSingleOrder...{}", response);
        return response;
    }


}

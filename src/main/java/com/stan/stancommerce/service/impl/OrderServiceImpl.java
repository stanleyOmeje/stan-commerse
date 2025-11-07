package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.CheckoutRequest;
import com.stan.stancommerce.dto.OrderDto;
import com.stan.stancommerce.dto.response.CheckoutResponse;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.entities.Cart;
import com.stan.stancommerce.entities.Orders;
import com.stan.stancommerce.entities.User;
import com.stan.stancommerce.enums.ResponseStatus;
import com.stan.stancommerce.exception.NotFoundException;
import com.stan.stancommerce.exception.PaymentException;
import com.stan.stancommerce.exception.UnAuthorizedException;
import com.stan.stancommerce.mapper.OrderMapper;
import com.stan.stancommerce.repositories.CartRepository;
import com.stan.stancommerce.repositories.OrderRepository;
import com.stan.stancommerce.repositories.UserRepository;
import com.stan.stancommerce.service.CartService;
import com.stan.stancommerce.service.OrderService;
import com.stan.stancommerce.service.PaymentGateway;
import com.stan.stancommerce.service.security.authservice.AuthService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    @Value("${stripe.baseUrl: }")
    String baseUrl;

    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final AuthService authService;
    private final PaymentGateway paymentGateway;


    @Transactional
    @Override
    public DefaultResponse<?> checkout(CheckoutRequest checkoutRequest) {

        log.debug("Inside OrderServiceImpl::checkout with Checkout request: {}", checkoutRequest);
        DefaultResponse<CheckoutResponse> response = new DefaultResponse<>();
        CheckoutResponse checkoutResponse = null;
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
            response = new DefaultResponse<>();
            response.setStatus(ResponseStatus.NOT_FOUND.getCode());
            response.setMessage(ResponseStatus.NOT_FOUND.getMessage());
            return response;
        }
        Orders order = orderMapper.mapCartToOrder(cart);
        try {
            order = orderRepository.save(order);
            if (order.getId() != null) {
                response.setStatus(ResponseStatus.SUCCESS.getMessage());
                response.setMessage(ResponseStatus.SUCCESS.getMessage());
                checkoutResponse = new CheckoutResponse();
                checkoutResponse.setOrderId(order.getId());

                String checkoutUrl = paymentGateway.pay(order);
                checkoutResponse.setCheckUrl(checkoutUrl);
                response.setData(checkoutResponse);

                cartService.clearCart(cart.getId());

                log.debug("OrderServiceImpl::checkout response: {}", response);
                return response;
            }
        } catch (PaymentException e) {
            response.setStatus(e.getCode());
            response.setMessage(e.getMessage());
            orderRepository.delete(order);
            return response;
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
        if (!order.isPlacedBy(user)) {
            throw new UnAuthorizedException(ResponseStatus.UNAUTHORIZED.getMessage());
        }
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(orderMapper.mapOrderToOrderDto(order));
        log.info("getSingleOrder...{}", response);
        return response;
    }


}

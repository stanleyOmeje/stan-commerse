package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.WebhookPaymentRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.dto.response.WebhookPaymentResponse;
import com.stan.stancommerce.entities.Orders;
import com.stan.stancommerce.enums.OrderStatus;
import com.stan.stancommerce.exception.NotFoundException;
import com.stan.stancommerce.repositories.OrderRepository;
import com.stan.stancommerce.service.PaymentGateway;
import com.stan.stancommerce.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Objects;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    @Override
    public DefaultResponse<?> paymentWebhook(WebhookPaymentRequest webhookPaymentRequest) {
        log.info("Inside PaymentServiceImpl::paymentWebhook with WebhookPaymentRequest sent to payment webhook : {}", webhookPaymentRequest);
        WebhookPaymentResponse webhookPaymentResponse = new WebhookPaymentResponse();
        try {
            webhookPaymentResponse = paymentGateway.paymentWebbook(webhookPaymentRequest);
            Long orderId = webhookPaymentResponse.getOrderId();
            if (Objects.isNull(orderId)) {
                throw new NotFoundException("Order not found");
            }
            Optional<Orders> optionalOrders = orderRepository.findById(orderId);
            if (optionalOrders.isPresent()) {
                Orders orders = optionalOrders.get();
                orders.setStatus(OrderStatus.PAID);
                log.info("Successfully processed payment intent");
                DefaultResponse<Orders> response = new DefaultResponse<>();
                response.setStatus("00");
                response.setMessage("Successfully processed payment: Order id : " + orderId);
                response.setData(orders);
                return response;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }
}

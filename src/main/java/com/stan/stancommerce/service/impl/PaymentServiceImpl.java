package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.WebhookPaymentRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.entities.Orders;
import com.stan.stancommerce.enums.OrderStatus;
import com.stan.stancommerce.repositories.OrderRepository;
import com.stan.stancommerce.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Optional;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    @Value("${stripe.webhookKey}")
    private String webhookSecretKey;

    public PaymentServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public DefaultResponse paymentWebhook(WebhookPaymentRequest webhookPaymentRequest) {
        log.info("Inside PaymentServiceImpl::paymentWebhook with WebhookPaymentRequest sent to payment webhook : {}", webhookPaymentRequest);
        String signature = webhookPaymentRequest.getHeaders().get("stripe-signature");
        String payload = webhookPaymentRequest.getPayload();
        try {
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
            var dataObject = event.getDataObjectDeserializer().getObject().orElseThrow(() ->
                new SignatureVerificationException("Error", signature));
            var eventType = event.getType();
            log.info("eventType...{}", eventType);
            switch (event.getType()) {
                case "charge.succeeded" -> {
                    //PaymentIntent paymentIntent = (PaymentIntent) dataObject;
                    Charge paymentIntent = (Charge) dataObject;

                    final StopWatch sw = new StopWatch("paymentWebhook");

                    sw.start("getOrderId");
                    String orderId = paymentIntent.getMetadata().get("order_id");
                    log.info("Totat time to ger Order id : {}", sw.getTotalTimeMillis());
                    sw.stop();

                    Optional<Orders> optionalOrders = orderRepository.findById(Long.parseLong(orderId));
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
                }
                case "payment_intent.failed" -> {
                    PaymentIntent paymentIntent = (PaymentIntent) dataObject;
                    String orderId = paymentIntent.getMetadata().get("order_id");
                    Optional<Orders> optionalOrders = orderRepository.findById(Long.parseLong(orderId));
                    if (optionalOrders.isPresent()) {
                        Orders orders = optionalOrders.get();
                        orders.setStatus(OrderStatus.FAILED);
                        log.info("Unable to process payment");
                        DefaultResponse<Orders> response = new DefaultResponse<>();
                        response.setStatus("01");
                        response.setMessage("Unable to process payment");
                        return response;
                    }

                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }
}

package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.WebhookPaymentRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.dto.response.WebhookPaymentResponse;
import com.stan.stancommerce.entities.Orders;
import com.stan.stancommerce.enums.OrderStatus;
import com.stan.stancommerce.enums.ResponseStatus;
import com.stan.stancommerce.exception.PaymentException;
import com.stan.stancommerce.service.PaymentGateway;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class StripePaymentGateway implements PaymentGateway {

    @Value("${stripe.webhookKey}")
    private String webhookSecretKey;


    @Override
    public String pay(Orders order) {
        try {
            var builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:9005/success/")
                .setCancelUrl("http://localhost:9005/cancel/" + order.getId())
                .setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                        .putMetadata("order_id",String.valueOf(order.getId()))
                        .build()
                );

            order.getItems().forEach(orderItems -> {
                var lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(Long.valueOf(orderItems.getQuantity()))
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("USD")
                            .setUnitAmountDecimal(orderItems.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(orderItems.getProduct().getName())
                                    .setDescription(orderItems.getProduct().getDescription()).build()
                            ).build()
                    ).build();
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());
            return session.getUrl();
        } catch (StripeException e) {
            throw new PaymentException(ResponseStatus.PAYMENT_FAILED.getCode(),ResponseStatus.PAYMENT_FAILED.getMessage());
        }
        catch (Exception e) {
         log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public WebhookPaymentResponse paymentWebbook(WebhookPaymentRequest webhookPaymentRequest) {
        WebhookPaymentResponse response = new WebhookPaymentResponse();
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
                    Charge paymentIntent = (Charge) dataObject;
                    final StopWatch sw = new StopWatch("paymentWebhook");
                    sw.start("getOrderId");
                    String orderId = paymentIntent.getMetadata().get("order_id");
                    log.info("Totat time to ger Order id : {}", sw.getTotalTimeMillis());
                    sw.stop();
                    response.setOrderId(Long.parseLong(orderId));
                    response.setOrderStatus(paymentIntent.getStatus());

                }
                case "payment_intent.succeeded" -> {
                    PaymentIntent paymentIntent = (PaymentIntent) dataObject;
                    final StopWatch sw = new StopWatch("paymentWebhook");
                    sw.start("getOrderId");
                    String orderId = paymentIntent.getMetadata().get("order_id");
                    log.info("Totat time to ger Order id : {}", sw.getTotalTimeMillis());
                    sw.stop();
                    response.setOrderId(Long.parseLong(orderId));
                    response.setOrderStatus(paymentIntent.getStatus());

                }
                case "payment_intent.failed" -> {
                    PaymentIntent paymentIntent = (PaymentIntent) dataObject;
                    String orderId = paymentIntent.getMetadata().get("order_id");
                    response.setOrderId(Long.parseLong(orderId));
                    response.setOrderStatus(paymentIntent.getStatus());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }
}

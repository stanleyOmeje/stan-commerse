package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.entities.Orders;
import com.stan.stancommerce.enums.ResponseStatus;
import com.stan.stancommerce.exception.PaymentException;
import com.stan.stancommerce.service.PaymentGateway;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Slf4j
@Service
public class StripePaymentGateway implements PaymentGateway {
    @Override
    public String pay(Orders order) {
        try {
            var builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success/" + order.getId())
                .setCancelUrl("http://localhost:8080/cancel/" + order.getId())
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
}

package com.stan.stancommerce.service;

import com.stan.stancommerce.entities.Orders;

public interface PaymentGateway {
    String pay(Orders orders);
}

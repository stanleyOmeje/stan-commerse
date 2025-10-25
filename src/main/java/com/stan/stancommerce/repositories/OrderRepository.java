package com.stan.stancommerce.repositories;

import com.stan.stancommerce.entities.Cart;
import com.stan.stancommerce.entities.Orders;
import com.stan.stancommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByCustomer(User customer);
}

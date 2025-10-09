package com.stan.stancommerce.repositories;

import com.stan.stancommerce.entities.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Long> {
}

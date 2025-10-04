package com.stan.stancommerce.repositories;


import com.stan.stancommerce.entities.Category;
import com.stan.stancommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    List<Product> findByCategory(Category category);
}

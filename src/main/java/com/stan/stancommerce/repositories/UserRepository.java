package com.stan.stancommerce.repositories;


import com.stan.stancommerce.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}

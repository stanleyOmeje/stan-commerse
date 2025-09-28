package com.stan.stancommerce.repositories;


import com.stan.stancommerce.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}

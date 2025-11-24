package com.stan.stancommerce.repositories;

import com.stan.stancommerce.entities.MerchantDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantDetailsRepository extends JpaRepository<MerchantDetails, Long> {
}

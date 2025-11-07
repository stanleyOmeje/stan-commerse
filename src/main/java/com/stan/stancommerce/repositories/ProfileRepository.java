package com.stan.stancommerce.repositories;


import com.stan.stancommerce.entities.Profile;
import com.stan.stancommerce.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);
}

package com.stan.stancommerce.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateCreated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE)
    private Set<CartItems> cartItems = new HashSet<>();
}

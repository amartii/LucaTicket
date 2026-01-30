package com.ejemplos.spring.cart.repository.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplos.spring.cart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}

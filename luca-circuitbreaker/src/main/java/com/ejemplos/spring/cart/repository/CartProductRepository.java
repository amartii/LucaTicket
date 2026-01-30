package com.ejemplos.spring.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplos.spring.cart.model.CartProduct;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
}

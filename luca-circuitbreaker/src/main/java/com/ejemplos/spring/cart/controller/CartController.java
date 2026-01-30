package com.ejemplos.spring.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;

import com.ejemplos.spring.cart.repository.service.CartRepository;
import com.ejemplos.spring.cart.response.CartResponse;

import com.ejemplos.spring.cart.service.CartService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.List;

@RestController
@RequestMapping("/cartCB")
public class CartController {

    @Autowired 
    private CartRepository cartRepository;
    
    @Autowired 
    private CartService cartService;

    //@Autowired private RestTemplate restTemplate;
    
    @GetMapping("/")
    public List<CartResponse> getCartAll() {
        return CartResponse.of(cartRepository.findAll());
    }

    @GetMapping("/{id}")
    public CartResponse getCart(@PathVariable Long id) {
        return CartResponse.of(cartRepository.findById(id).orElseThrow());
    }

    //La llamaremos  
    //    /1/add?idProduct=2
    //    /1/add?idProduct=2&qt=8
    
    // He remodelado el método para que dé respuestas
    // Este fallará porque no levantaremos el Catalogo 
    @CircuitBreaker(name = "productCB", fallbackMethod = "fallBackAddProduct")
    @PostMapping("/{id}/add")
    public ResponseEntity<CartResponse> addProduct(@PathVariable Long id, @RequestParam Long idProduct, @RequestParam(defaultValue = "1") Integer qt) {
        cartService.addProduct(id, idProduct, qt);      
        CartResponse cartResponse = CartResponse.of(cartRepository.findById(id).orElseThrow());
        if (cartResponse == null) {
        	return ResponseEntity.badRequest().build();
        }
        else {
        	return ResponseEntity.ok(cartResponse);
        }
    }
    
    // Creo el metodo para Circuit Breaker que incluye la misma salida que el metodo anterior
    // Los parametros de entrada son los mismos y añado una excepcion al finaL
    private ResponseEntity<CartResponse> fallBackAddProduct(@PathVariable Long id, @RequestParam Long idProduct, @RequestParam(defaultValue = "1") Integer qt, RuntimeException e) {
    	System.out.println("----------- fallBackAddProduct");
    	return new ResponseEntity("Catalogo no disponible actualmente. Intentelo más tarde", HttpStatus.OK); 

    }
}

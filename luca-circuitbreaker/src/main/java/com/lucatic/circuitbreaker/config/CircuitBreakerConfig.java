package com.lucatic.circuitbreaker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

/**
 * Configuración centralizada de Circuit Breakers para LucaTicket
 * 
 * Esta clase define los circuit breakers que se usarán para proteger
 * las llamadas a los diferentes microservicios
 */
@Configuration
public class CircuitBreakerConfig {

    /**
     * Bean de CircuitBreakerRegistry para registrar y gestionar
     * todos los circuit breakers de la aplicación
     */
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }
}

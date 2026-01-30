package com.lucatic.circuitbreaker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucatic.circuitbreaker.service.CompraServiceWrapper;
import com.lucatic.circuitbreaker.service.EventosServiceWrapper;

/**
 * Controlador que actúa como proxy con Circuit Breaker
 * Expone endpoints que integran la lógica de protección ante fallos
 */
@RestController
@RequestMapping("/api")
public class CircuitBreakerController {

    private static final Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @Autowired
    private EventosServiceWrapper eventosServiceWrapper;

    @Autowired
    private CompraServiceWrapper compraServiceWrapper;

    // ============ EVENTOS ENDPOINTS ============

    /**
     * Obtiene un evento específico
     */
    @GetMapping("/eventos/{id}")
    public ResponseEntity<?> getEvento(@PathVariable Long id) {
        logger.info("GET /api/eventos/{}", id);
        Object evento = eventosServiceWrapper.getEvento(id);
        
        if (evento == null) {
            return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de eventos no disponible en este momento");
        }
        return ResponseEntity.ok(evento);
    }

    /**
     * Obtiene todos los eventos
     */
    @GetMapping("/eventos")
    public ResponseEntity<?> getAllEventos() {
        logger.info("GET /api/eventos");
        Object eventos = eventosServiceWrapper.getAllEventos();
        
        if (eventos == null) {
            return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de eventos no disponible en este momento");
        }
        return ResponseEntity.ok(eventos);
    }

    /**
     * Crea un nuevo evento
     */
    @PostMapping("/eventos")
    public ResponseEntity<?> createEvento(@RequestBody Object evento) {
        logger.info("POST /api/eventos");
        Object result = eventosServiceWrapper.createEvento(evento);
        
        if (result == null) {
            return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de eventos no disponible en este momento");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // ============ COMPRAS ENDPOINTS ============

    /**
     * Obtiene una compra específica
     */
    @GetMapping("/compras/{id}")
    public ResponseEntity<?> getCompra(@PathVariable Long id) {
        logger.info("GET /api/compras/{}", id);
        Object compra = compraServiceWrapper.getCompra(id);
        
        if (compra == null) {
            return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de compras no disponible en este momento");
        }
        return ResponseEntity.ok(compra);
    }

    /**
     * Obtiene todas las compras
     */
    @GetMapping("/compras")
    public ResponseEntity<?> getAllCompras() {
        logger.info("GET /api/compras");
        Object compras = compraServiceWrapper.getAllCompras();
        
        if (compras == null) {
            return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de compras no disponible en este momento");
        }
        return ResponseEntity.ok(compras);
    }

    /**
     * Procesa una nueva compra
     */
    @PostMapping("/compras")
    public ResponseEntity<?> procesarCompra(@RequestBody Object compra) {
        logger.info("POST /api/compras");
        Object result = compraServiceWrapper.procesarCompra(compra);
        
        if (result == null) {
            return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de compras no disponible en este momento");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // ============ HEALTH CHECK ============

    /**
     * Endpoint para verificar el estado del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Circuit Breaker Service is running");
    }
}

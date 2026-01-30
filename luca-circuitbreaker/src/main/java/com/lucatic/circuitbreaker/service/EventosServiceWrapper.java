package com.lucatic.circuitbreaker.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lucatic.circuitbreaker.feign.EventosServiceFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio para interactuar con el servicio de Eventos
 * Incluye protección mediante Circuit Breaker
 */
@Service
public class EventosServiceWrapper {

    private static final Logger logger = LoggerFactory.getLogger(EventosServiceWrapper.class);
    
    @Autowired
    private EventosServiceFeignClient eventosServiceFeignClient;

    /**
     * Obtiene un evento con protección de Circuit Breaker
     * @param id ID del evento
     * @return Datos del evento o null si falla
     */
    @CircuitBreaker(name = "eventosServiceCB", fallbackMethod = "fallbackGetEvento")
    public Object getEvento(Long id) {
        logger.info("Obteniendo evento con ID: {}", id);
        return eventosServiceFeignClient.getEvento(id);
    }

    /**
     * Obtiene todos los eventos con protección de Circuit Breaker
     * @return Lista de eventos o null si falla
     */
    @CircuitBreaker(name = "eventosServiceCB", fallbackMethod = "fallbackGetAllEventos")
    public Object getAllEventos() {
        logger.info("Obteniendo todos los eventos");
        return eventosServiceFeignClient.getAllEventos();
    }

    /**
     * Crea un nuevo evento con protección de Circuit Breaker
     * @param evento Datos del evento
     * @return Evento creado o null si falla
     */
    @CircuitBreaker(name = "eventosServiceCB", fallbackMethod = "fallbackCreateEvento")
    public Object createEvento(Object evento) {
        logger.info("Creando nuevo evento");
        return eventosServiceFeignClient.createEvento(evento);
    }

    // ============ FALLBACK METHODS ============

    /**
     * Fallback para getEvento
     */
    public Object fallbackGetEvento(Long id, Exception e) {
        logger.error("Circuit Breaker activado para getEvento. Servicio de Eventos no disponible", e);
        return null;
    }

    /**
     * Fallback para getAllEventos
     */
    public Object fallbackGetAllEventos(Exception e) {
        logger.error("Circuit Breaker activado para getAllEventos. Servicio de Eventos no disponible", e);
        return null;
    }

    /**
     * Fallback para createEvento
     */
    public Object fallbackCreateEvento(Object evento, Exception e) {
        logger.error("Circuit Breaker activado para createEvento. Servicio de Eventos no disponible", e);
        return null;
    }
}

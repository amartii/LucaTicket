package com.lucatic.circuitbreaker.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lucatic.circuitbreaker.feign.CompraServiceFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio para interactuar con el servicio de Compras
 * Incluye protección mediante Circuit Breaker
 */
@Service
public class CompraServiceWrapper {

    private static final Logger logger = LoggerFactory.getLogger(CompraServiceWrapper.class);
    
    @Autowired
    private CompraServiceFeignClient compraServiceFeignClient;

    /**
     * Obtiene una compra con protección de Circuit Breaker
     * @param id ID de la compra
     * @return Datos de la compra o null si falla
     */
    @CircuitBreaker(name = "compraServiceCB", fallbackMethod = "fallbackGetCompra")
    public Object getCompra(Long id) {
        logger.info("Obteniendo compra con ID: {}", id);
        return compraServiceFeignClient.getCompra(id);
    }

    /**
     * Obtiene todas las compras con protección de Circuit Breaker
     * @return Lista de compras o null si falla
     */
    @CircuitBreaker(name = "compraServiceCB", fallbackMethod = "fallbackGetAllCompras")
    public Object getAllCompras() {
        logger.info("Obteniendo todas las compras");
        return compraServiceFeignClient.getAllCompras();
    }

    /**
     * Procesa una compra con protección de Circuit Breaker
     * @param compra Datos de la compra
     * @return Resultado del procesamiento o null si falla
     */
    @CircuitBreaker(name = "compraServiceCB", fallbackMethod = "fallbackProcesarCompra")
    public Object procesarCompra(Object compra) {
        logger.info("Procesando nueva compra");
        return compraServiceFeignClient.procesarCompra(compra);
    }

    // ============ FALLBACK METHODS ============

    /**
     * Fallback para getCompra
     */
    public Object fallbackGetCompra(Long id, Exception e) {
        logger.error("Circuit Breaker activado para getCompra. Servicio de Compras no disponible", e);
        return null;
    }

    /**
     * Fallback para getAllCompras
     */
    public Object fallbackGetAllCompras(Exception e) {
        logger.error("Circuit Breaker activado para getAllCompras. Servicio de Compras no disponible", e);
        return null;
    }

    /**
     * Fallback para procesarCompra
     */
    public Object fallbackProcesarCompra(Object compra, Exception e) {
        logger.error("Circuit Breaker activado para procesarCompra. Servicio de Compras no disponible", e);
        return null;
    }
}

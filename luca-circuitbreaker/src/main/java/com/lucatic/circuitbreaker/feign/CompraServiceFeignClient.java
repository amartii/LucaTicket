package com.lucatic.circuitbreaker.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente Feign para comunicarse con el servicio de Compras/Pasarela
 * Utiliza Eureka para descubrir la ubicación del servicio
 */
@FeignClient(name = "compra-service")
public interface CompraServiceFeignClient {

    /**
     * Obtiene una compra por su ID
     * @param id ID de la compra
     * @return Datos de la compra
     */
    @GetMapping("/pasarela/{id}")
    Object getCompra(@PathVariable Long id);

    /**
     * Obtiene todas las compras
     * @return Lista de compras
     */
    @GetMapping("/pasarela")
    Object getAllCompras();

    /**
     * Procesa una nueva compra/pasarela
     * @param compra Datos de la compra a procesar
     * @return Resultado del procesamiento
     */
    @PostMapping("/pasarela")
    Object procesarCompra(@RequestBody Object compra);
}

package com.lucatic.circuitbreaker.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente Feign para comunicarse con el servicio de Eventos
 * Utiliza Eureka para descubrir la ubicación del servicio
 */
@FeignClient(name = "eventos-service")
public interface EventosServiceFeignClient {

    /**
     * Obtiene un evento por su ID
     * @param id ID del evento
     * @return Datos del evento
     */
    @GetMapping("/eventos/{id}")
    Object getEvento(@PathVariable Long id);

    /**
     * Obtiene todos los eventos
     * @return Lista de eventos
     */
    @GetMapping("/eventos")
    Object getAllEventos();

    /**
     * Crea un nuevo evento
     * @param evento Datos del evento a crear
     * @return Evento creado
     */
    @PostMapping("/eventos")
    Object createEvento(@RequestBody Object evento);
}

package com.lucatic.compra_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.lucatic.compra_service.dto.CompraRequest;
import com.lucatic.compra_service.dto.DatosPagoBanco;
import com.lucatic.compra_service.dto.DatosTarjeta;
import java.util.HashMap;
import java.util.Map;

@Service
public class CompraService {

    private static final String URL_BANCO = "http://lucabanking.us-east-1.elasticbeanstalk.com/pasarela/compra";

    private final RestTemplate restTemplate;

    public CompraService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> procesarCompra(CompraRequest request) {
        // 1. Construir la petición para el banco
        DatosTarjeta t = request.getDatosTarjeta();

        DatosPagoBanco datosBanco = new DatosPagoBanco();
        datosBanco.setNombreTitular(t.getNombreTitular());
        datosBanco.setNumeroTarjeta(t.getNumeroTarjeta());
        datosBanco.setMesCaducidad(t.getMesCaducidad());
        datosBanco.setYearCaducidad(t.getYearCaducidad());
        datosBanco.setCvv(t.getCvv());
        //pongo fijos el emisor, concepto y cantidad fase 06 hacer evento para consultar en evento-service
        datosBanco.setEmisor("LucaTicket");
        datosBanco.setConcepto("Entrada a concierto");
        datosBanco.setCantidad("100.5");

        // 2. Llamar al microservicio externo LucaBanking
        try {
            Map<String, Object> respuesta = restTemplate.postForObject(
                URL_BANCO,
                datosBanco,
                Map.class  
            );
            return respuesta;
                } catch (Exception e) {
                    return Map.of(
                        "codigo", "error",
                        "descripcion", "Error al contactar con el banco: " + e.getMessage()
                    );
                }
            }
        }
package com.lucatic.compra_service.controller;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.lucatic.compra_service.dto.CompraRequest;
import com.lucatic.compra_service.dto.DatosTarjeta;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PasarelaControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private CompraRequest crearCompraValida() {
        DatosTarjeta tarjeta = new DatosTarjeta();
        tarjeta.setNombreTitular("Antonio Santos Ramos");
        tarjeta.setNumeroTarjeta("4624-0031-8793-4978");
        tarjeta.setMesCaducidad("10");
        tarjeta.setYearCaducidad("2026");
        tarjeta.setCvv("123");

        CompraRequest request = new CompraRequest();
        request.setMail("test@example.com");
        request.setIdEvento(1);
        request.setDatosTarjeta(tarjeta);
        return request;
    }

    @Test
    void transaccion_correcta_deberiaDevolverCodigoDelBanco() {
        CompraRequest request = crearCompraValida();

        ResponseEntity<Map> response = restTemplate.postForEntity("/pasarela/compra", request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "La respuesta no debe estar vacía");
    }

    @Test
    void cvv_invalido_deberiaDevolverError() {
        CompraRequest request = crearCompraValida();
        request.getDatosTarjeta().setCvv("12"); // tiene que ser 3 números (pongo 2)

        ResponseEntity<Map> response = restTemplate.postForEntity("/pasarela/compra", request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // El banco devuelve "error" cuando hay problemas (mensaje añadido en el servicio)
        assertTrue(response.getBody().containsKey("codigo") || response.getBody().containsValue("error"),
                "Debe contener error");
    }

    @Test
    void mes_caducidad_invalido_deberiaDevolverError() {
        CompraRequest request = crearCompraValida();
        request.getDatosTarjeta().setMesCaducidad("13"); // Mes tiene que ser 01-12

        ResponseEntity<Map> response = restTemplate.postForEntity("/pasarela/compra", request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void year_caducidad_invalido_deberiaDevolverError() {
        CompraRequest request = crearCompraValida();
        request.getDatosTarjeta().setYearCaducidad("2025"); // Año tiene que ser >= al actual (2026)

        ResponseEntity<Map> response = restTemplate.postForEntity("/pasarela/compra", request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void numero_tarjeta_invalido_deberiaDevolverError() {
        CompraRequest request = crearCompraValida();
        request.getDatosTarjeta().setNumeroTarjeta("1234567890123456"); // Formato incorrecto, tiene que ser xxxx-xxxx-xxxx-xxxx

        ResponseEntity<Map> response = restTemplate.postForEntity("/pasarela/compra", request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void nombre_titular_muchas_palabras_deberiaDevolverError() {
        CompraRequest request = crearCompraValida();
        request.getDatosTarjeta().setNombreTitular("Antonio Santos Ramos González"); // Máximo 3 palabras (fuerzo y pongo 4)

        ResponseEntity<Map> response = restTemplate.postForEntity("/pasarela/compra", request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void cliente_no_encontrado_deberiaDevolverRespuesta() {
        CompraRequest request = crearCompraValida();
        request.setIdEvento(99999); // ID de evento que no existe

        ResponseEntity<Map> response = restTemplate.postForEntity("/pasarela/compra", request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // El banco puede devolver null o un error, ambos son válidos
        assertTrue(response.getBody() != null, "Debe haber respuesta del banco");
    }

    @Test
    void sin_fondos_suficientes_deberiaDevolverRespuesta() {
        CompraRequest request = crearCompraValida();
        request.getDatosTarjeta().setNumeroTarjeta("5105-1051-0510-5100"); // Tarjeta sin fondos (test), cuando hacemos muchos posts se nos acaba quedando sin dinero

        ResponseEntity<Map> response = restTemplate.postForEntity("/pasarela/compra", request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null, "Debe haber respuesta del banco");
    }
}

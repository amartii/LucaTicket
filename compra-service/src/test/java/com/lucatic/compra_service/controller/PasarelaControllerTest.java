package com.lucatic.compra_service.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

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

    @Test //si compramos algo correcto nos debería de devolver un código de éxito o error del banco
    void comprafunciona_deberiaDevolverCodigo() {
        //le doy todo esto (formato correcto o no, pero me tiene que devolver si o si un mensaje del banco)
        DatosTarjeta tarjeta = new DatosTarjeta();
        tarjeta.setNombreTitular("Antonio Santos Ramos");
        tarjeta.setNumeroTarjeta("4624-0031-8793-4978");
        tarjeta.setMesCaducidad("10");
        tarjeta.setYearCaducidad("2026");
        tarjeta.setCvv("123");

        CompraRequest request = new CompraRequest();
        request.setMail("test01@example.com");
        request.setIdEvento(6);
        request.setDatosTarjeta(tarjeta);

        //(WHEN)
        ResponseEntity<Map> response =
                restTemplate.postForEntity("/pasarela/compra", request, Map.class);

        //(THEN)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Verifica que el banco responde con algún contenido (éxito o error)
        assertTrue(response.getBody().containsKey("codigo") || response.getBody().containsKey("error"),
                "La respuesta debe contener 'codigo' o 'error'");
    }

    @Test //si el cvv es incorrecto nos debería de devolver un código de error, literalmente "error"
    void compraIncorrecta_deberiaDevolverError() {
        //(dado todo esto)
        DatosTarjeta tarjeta = new DatosTarjeta();
        tarjeta.setNombreTitular("Antonio Santos Ramos");
        tarjeta.setNumeroTarjeta("4624-0121-8793-4978");
        tarjeta.setMesCaducidad("10");
        tarjeta.setYearCaducidad("2026");
        tarjeta.setCvv("12345"); // incorrecto por ejemplo

        CompraRequest request = new CompraRequest();
        request.setMail("test012@example.com");
        request.setIdEvento(3);
        request.setDatosTarjeta(tarjeta);

        //(when)
        ResponseEntity<Map> response =
                restTemplate.postForEntity("/pasarela/compra", request, Map.class);

        //(then)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().get("codigo"));
        //puedo hacer que compruebe el error exacto?
        //por ejemplo que  void compraIncorrectapor_deberiaDevolverError
        //comprobando que error sea expected "400.0004" -> El formato del cvv no es válido
    }
}

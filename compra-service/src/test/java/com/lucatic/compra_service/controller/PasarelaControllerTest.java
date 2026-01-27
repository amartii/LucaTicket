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

    @Test //si compramos exitosamente nos debería de devolver un 200 ok y nos devuelve el chorrón de código
    void compraExitosa_deberiaDevolverCodigo() {
        //todo esto
        DatosTarjeta tarjeta = new DatosTarjeta();
        tarjeta.setNombreTitular("Antonio Santos Ramos");
        tarjeta.setNumeroTarjeta("4624-0031-8793-4978");
        tarjeta.setMesCaducidad("11");
        tarjeta.setYearCaducidad("2026");
        tarjeta.setCvv("123");

        CompraRequest request = new CompraRequest();
        request.setMail("test01@example.com");
        request.setIdEvento(1);
        request.setDatosTarjeta(tarjeta);

        //(WHEN)
        ResponseEntity<Map> response =
                restTemplate.postForEntity("/pasarela/compra", request, Map.class);

        //(THEN)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("codigo"));
    }

    @Test //si el cvv es incorrecto nos debería de devolver un código de error, literalmente "error"
    void compraIncorrecta_deberiaDevolverError() {
        //(dado todo esto)
        DatosTarjeta tarjeta = new DatosTarjeta();
        tarjeta.setNombreTitular("Antonio Santos Ramos");
        tarjeta.setNumeroTarjeta("4624-0071-8793-4978");
        tarjeta.setMesCaducidad("11");
        tarjeta.setYearCaducidad("2026");
        tarjeta.setCvv("12"); // incorrecto por ejemplo

        CompraRequest request = new CompraRequest();
        request.setMail("test01@example.com");
        request.setIdEvento(1);
        request.setDatosTarjeta(tarjeta);

        //(when)
        ResponseEntity<Map> response =
                restTemplate.postForEntity("/pasarela/compra", request, Map.class);

        //(then)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().get("codigo"));
        //puedo hacer que compruebe el error exacto?
        //por ejemplo que  void compraIncorrectaporCVincorrecto_deberiaDevolverError
        //comprobando que error sea expected "400.0004" -> El formato del cvv no es válido
    }
}

package com.lucatic.compra_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lucatic.compra_service.dto.CompraRequest;
import com.lucatic.compra_service.service.CompraService;
import java.util.Map;

@RestController
@RequestMapping("/pasarela")
public class PasarelaController {

    private final CompraService compraService;

    public PasarelaController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping("/compra")
    public ResponseEntity<Map<String, Object>> comprar(@RequestBody CompraRequest request) {
        Map<String, Object> respuestaBanco = compraService.procesarCompra(request);
        return ResponseEntity.ok(respuestaBanco);
    }
}

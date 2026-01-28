package com.lucatic.compra_service.service;

import java.util.Map;

import com.lucatic.compra_service.dto.CompraRequest;

public interface ICompraService {
    Map<String, Object> procesarCompra(CompraRequest request);
}

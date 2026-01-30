package com.ejemplos.spring.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ejemplos.spring.cart.response.ProductResponse;



//Se añade este nombre : spring.application.name=catalog
//Indico que voy a contactar con esa ruta
@FeignClient(name = "catalog", url= "http://localhost:7777")
public interface CatalogFeignClient {
	
    @GetMapping("/product/{id}")
    public ProductResponse getProduct (@PathVariable Long id);

}

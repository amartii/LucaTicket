package com.ejemplos.spring.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;

import com.ejemplos.spring.cart.model.Cart;
import com.ejemplos.spring.cart.model.CartProduct;
import com.ejemplos.spring.cart.repository.CartProductRepository;
import com.ejemplos.spring.cart.repository.service.CartRepository;
import com.ejemplos.spring.cart.response.ProductResponse;
import com.ejemplos.spring.feignclients.CatalogFeignClient;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;

@Transactional
@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartProductRepository cartProductRepository;

	//@Autowired (OLD)
	//private RestTemplate restTemplate;
	
	@Autowired
	private CatalogFeignClient catalogFeign;

	public void addProduct(Long id, Long idProduct, Integer qt) {
		
		//Antes
		//   final ProductResponse product = restTemplate.getForObject("http://catalog/product/" + idProduct,ProductResponse.class);
		//Ahora usamos OpenFeign
		final ProductResponse product = catalogFeign.getProduct(idProduct);

		this.addProduct(id, idProduct, product.getPrice(), product.getName(), qt);

	}

	public void addProduct(Long id, Long idProduct, BigDecimal price, String name, Integer qt) {
		final Cart cart = cartRepository.findById(id).orElseThrow();

		for (int i = 0; i < qt; i++) {
			CartProduct cp = new CartProduct();
			cp.setCart(cart);
			cp.setProduct_id(idProduct);
			cp.setPrice(price);
			cp.setName(name);
			cartProductRepository.save(cp);
		}

	}
}

package com.ejemplos.spring.cart.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ejemplos.spring.cart.model.CartProduct;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

//Trabaja de DTO
public class ProductResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(ProductResponse.class);

	private Long id;
	private BigDecimal price;
	private String name;

	public static ProductResponse of(CartProduct product) {
		ProductResponse p = new ProductResponse();
		p.setId(product.getId());
		p.setName(product.getName());
		p.setPrice(product.getPrice());

		return p;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static List<ProductResponse> of(List<CartProduct> products) {
		log.info("----- Productos del carrito:" + products);
		return products.stream().map(p -> of(p)).collect(Collectors.toList());
	}

}

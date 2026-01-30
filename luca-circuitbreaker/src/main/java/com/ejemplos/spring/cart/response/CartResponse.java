package com.ejemplos.spring.cart.response;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.ejemplos.spring.cart.model.Cart;


//Trabaja de DTO
public class CartResponse implements Serializable {


	private static final long serialVersionUID = 1L;
	private Long id;
    private String user;
    private List<ProductResponse> products;

    public static CartResponse of(Cart cart) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(cart.getId());

        cartResponse.setProducts(ProductResponse.of(cart.getProducts()));

        cartResponse.setUser(cart.getUser().getName());

        return cartResponse;
    }
    
	public static List<CartResponse> of(List<Cart> carts) {
		return carts.stream().map(c -> of(c)).collect(Collectors.toList());
	}



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }
}

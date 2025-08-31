package com.example.model;

public class CartItemDTO {
	
	
    private Cart cart;
    private Product product;

    public CartItemDTO(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public Product getProduct() {
        return product;
    }

	@Override
	public String toString() {
		return "CartItemDTO [cart=" + cart + ", product=" + product + "]";
	}
    
    
}


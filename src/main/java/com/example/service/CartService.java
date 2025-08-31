package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.model.Cart;

import com.example.repository.CartRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {
	@Autowired
	CartRepository cr;

	/**
	 * Adds a cart item to the database. If the item already exists, it will be
	 * updated.
	 */
	public Cart addCart(Cart c) {
		return cr.save(c);
	}

	/**
	 * Retrieves a cart item for a user and product combination.
	 */
	public Cart getCartDetailsByUserIdAndProductId(int id, int productId) {
		return cr.findAllByUserIdAndProductId(id, productId);
	}

	/**
	 * Gets all cart items for a user.
	 */
	public List<Cart> getAllCartItemsByUserrId(int id) {
		return cr.findAllByUserId(id);
	}

	/**
	 * Deletes a cart item by its cartId.
	 */
	public boolean deleteCartItem(int cartId) {
		Cart c = cr.findById(cartId).get();
		if (c != null) {
			cr.delete(c);
			return true;
		}
		return false;
	}

	/**
	 * Gets all cart items for a user (duplicate of getAllCartItemsByUserrId).
	 */
	public List<Cart> getCartItems(int id) {
		return cr.findAllByUserId(id);
	}

	/**
	 * Deletes all cart items for a user using their user ID.
	 */
	@Transactional
	public String deleteAllCartItemsUsingUserrId(int id) {
		System.out.println(cr.deleteAllByUserId(id));
		return "DELETED SUCCESSFULLY";
	}
}

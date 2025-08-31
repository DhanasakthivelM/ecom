package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	Cart findAllByUserIdAndProductId(int id, int productId);

	List<Cart> findAllByUserId(int id);

	String deleteAllByUserId(int id);

}

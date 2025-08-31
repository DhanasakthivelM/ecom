package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Orders;
import com.example.repository.OrdersRepository;

@Service
public class OrdersService {

	@Autowired
	OrdersRepository ordersRepository;

	/**
	 * Adds a new order to the database.
	 */
	public Orders addOrder(Orders orders) {
		return ordersRepository.save(orders);
	}

	/**
	 * Retrieves all orders from the database.
	 */
	public List<Orders> getAllOrders() {
		return ordersRepository.findAll();
	}

	/**
	 * Retrieves all orders for a specific user by user ID.
	 */
	public List<Orders> getOrdersByUserId(int userId) {
		return ordersRepository.findAllByUserId(userId);
	}

}

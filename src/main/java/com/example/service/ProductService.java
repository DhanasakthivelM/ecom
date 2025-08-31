package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Product;
import com.example.repository.ProductRepo;

import java.util.List;

/**
 * Service class for managing product-related operations.
 */
@Service
public class ProductService {

	@Autowired
	private ProductRepo rp;

	/**
	 * Adds a new product to the database.
	 * 
	 * @param p Product object to add
	 * @return Saved Product object
	 */
	public Product addProduct(Product p) {
		return rp.save(p);
	}

	/**
	 * Retrieves all products from the database.
	 * 
	 * @return List of all Product objects
	 */
	public List<Product> getAllProducts() {
		return rp.findAll();
	}

	/**
	 * Retrieves a product by its ID.
	 * 
	 * @param id Product ID
	 * @return Product object if found, otherwise null
	 */
	public Product getProductById(int id) {
		return rp.findById(id);
	}

	/**
	 * Deletes a product by its ID.
	 *
	 * @param id Product ID
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteProductById(int id) {
		Product p = rp.findById(id);
		if (p != null) {
			rp.delete(p);
			return true;
		}
		return false;
	}

	/**
	 * Updates an existing product by ID.
	 *
	 * @param id Product ID to update
	 * @param p  Updated Product object
	 * @return Updated Product object, or null if not found
	 */
	public Product update(int id, Product p) {
		Product oldPro = rp.findById(id);
		if (oldPro != null) {
			return rp.save(p);
		}
		return null;
	}

	/**
	 * Retrieves all products by category name.
	 * 
	 * @param categoryName Category name to filter by
	 * @return List of Product objects in the category
	 */
	public List<Product> getAllProductsByCategoryName(String categoryName) {
		return rp.findByCategoryName(categoryName);
	}
}

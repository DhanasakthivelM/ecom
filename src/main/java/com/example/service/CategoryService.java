package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Category;
import com.example.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	CategoryRepository cr;

	/**
	 * Retrieves all categories from the database.
	 */
	public List<Category> getAllCategories() {
		return cr.findAll();
	}

	/**
	 * Retrieves a category by its ID.
	 */
	public Category getCategoryById(int id) {
		return cr.findById(id).orElse(null);
	}

	/**
	 * Deletes a category by its ID.
	 */
	public void deleteCategoryById(int id) {
		cr.deleteById(id);
	}

	/**
	 * Adds a new category to the database.
	 */
	public Category addCategory(Category category) {
		return cr.save(category);
	}

	/**
	 * Updates an existing category by ID.
	 */
	public Category updateCategory(int id, Category category) {
		Category c = cr.findById(id).orElse(null);
		if (c != null) {
			return cr.save(category);
		}
		return null;
	}
}

package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
import lombok.Data;
//import lombok.NoArgsConstructor;

@Entity
@Data
// @AllArgsConstructor
// @NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int productId;
	String name;
	String description;
	double price;
	String categoryName;
	int stockQuantity;
	String productImage;

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public Product(int productId, String name, String description, double price, String categoryName, int stockQuantity,
			String productImage) {
		super();
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.categoryName = categoryName;
		this.stockQuantity = stockQuantity;
		this.productImage = productImage;
	}

	public Product(String name, String description, double price, String categoryName, int stockQuantity,
			String productImage) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.categoryName = categoryName;
		this.stockQuantity = stockQuantity;
		this.productImage = productImage;
	}

	public Product() {
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", name=" + name + ", description=" + description + ", price="
				+ price + ", categoryName=" + categoryName + ", stockQuantity=" + stockQuantity + ", productImage="
				+ productImage + "]";
	}

}

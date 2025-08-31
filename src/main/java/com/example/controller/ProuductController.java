package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.Product;

import com.example.service.ProductService;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@Controller
public class ProuductController {

	@Autowired
	ProductService ps;

	/**
	 * Adds a new product. Handles image upload and saves product to the database.
	 * Only accessible by ADMIN users.
	 */
	@PostMapping("/products/add")
	public String addProduct(@ModelAttribute Product product,
			@RequestParam("imageFile") MultipartFile imageFile,
			RedirectAttributes redirectAttributes) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Check if user has ADMIN role
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

		if (!isAdmin) {
			redirectAttributes.addFlashAttribute("error", "Access denied. Admins only.");
			return "redirect:/index"; // or any error page
		}

		// Handle image upload
		if (!imageFile.isEmpty()) {
			String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
			// Save to src/main/resources/static/product-images/
			String devUploadDir = System.getProperty("user.dir") + "/src/main/resources/static/product-images/";
			File devDir = new File(devUploadDir);
			if (!devDir.exists())
				devDir.mkdirs();
			File devDest = new File(devUploadDir + fileName);
			// Save to target/classes/static/product-images/ (for production)
			String prodUploadDir = System.getProperty("user.dir") + "/target/classes/static/product-images/";
			File prodDir = new File(prodUploadDir);
			if (!prodDir.exists())
				prodDir.mkdirs();
			File prodDest = new File(prodUploadDir + fileName);
			try {
				imageFile.transferTo(devDest);
				java.nio.file.Files.copy(devDest.toPath(), prodDest.toPath(),
						java.nio.file.StandardCopyOption.REPLACE_EXISTING);
				product.setProductImage("/product-images/" + fileName);
			} catch (IOException e) {
				e.printStackTrace(); // Log error for debugging
				redirectAttributes.addFlashAttribute("error", "Image upload failed.");
				return "redirect:/allproducts";
			}
		}

		// Proceed to add product
		Product p = ps.addProduct(product); // Save to DB

		if (p != null) {
			redirectAttributes.addFlashAttribute("message", "Product added successfully!");
			return "redirect:/allproducts";
		}

		redirectAttributes.addFlashAttribute("error", "Failed to add product.");
		return "redirect:/allproducts";
	}

	/**
	 * Updates an existing product by ID.
	 */
	@PostMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") int id, @ModelAttribute Product product,
			RedirectAttributes redirectAttributes) {

		Product updated = ps.update(id, product);

		if (updated != null) {
			redirectAttributes.addFlashAttribute("message", "Product updated successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "Updates Failed ");
		}

		return "redirect:/allproducts";
	}

	/**
	 * Returns a list of all products as JSON (API endpoint).
	 */
	@GetMapping("/product")
	public List<Product> getAllProductDeatails() {

		return ps.getAllProducts();
	}

	/**
	 * Displays all products in the admin panel. Only accessible by ADMIN users.
	 */
	@GetMapping("/allproducts")
	public String allItems(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Check if user has ADMIN role
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

		if (!isAdmin) {
			return "redirect:/index"; // or any error page
		}

		List<Product> p = ps.getAllProducts();

		model.addAttribute("products", p);
		return "allproducts";

	}

	/**
	 * Displays products by category name for users. Requires authentication.
	 */
	@GetMapping("/products")
	public String getProductDeatailsByCategoryName(@RequestParam("name") String categoryName, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return "redirect:/req/login";
		}
		List<Product> listOfProducts = ps.getAllProductsByCategoryName(categoryName);
		model.addAttribute("productsList", listOfProducts);
		model.addAttribute("user", authentication.getName());
		return "products";
	}

	/**
	 * Returns a product by its ID as JSON (API endpoint).
	 */
	@GetMapping("/product/{id}")
	public Product getProductDeatails(@PathVariable("id") int productId) {
		return ps.getProductById(productId);
	}

	/**
	 * Deletes a product by its ID. Only accessible by authenticated users.
	 */
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return "redirect:/req/login"; // Redirect to login page if not authenticated
		}
		boolean deleted = ps.deleteProductById(id);
		if (deleted) {
			redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
			return "redirect:/allproducts"; // Redirect to the product list page
		}
		redirectAttributes.addFlashAttribute("error", "Product deleted Failed!");

		return "allproducts";
	}

}

package com.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.example.model.Category;
import com.example.model.Product;

import com.example.service.CategoryService;
import com.example.service.ProductService;

@Controller
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    /**
     * Displays the admin categories page with a list of all categories. Only
     * accessible by ADMIN users.
     */
    @GetMapping("/admincategories")
    public String listCategories(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if user has ADMIN role
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/index"; // or any error page
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admincategories";
    }

    /**
     * Shows the form to create a new category.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "category-form";
    }

    /**
     * Handles creation of a new category, including image upload. Only accessible
     * by ADMIN users.
     */
    @PostMapping("/categories/add")
    public String saveCategory(@ModelAttribute Category category,
            @RequestParam("imageFile") MultipartFile imageFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            return "redirect:/index";
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
                category.setCategoryImage("/product-images/" + fileName);
            } catch (IOException e) {
                e.printStackTrace(); // Log error for debugging
                category.setCategoryImage("");
            }
        }
        categoryService.addCategory(category);
        return "redirect:/admincategories";
    }

    /**
     * Updates an existing category by ID, with optional image upload. Only
     * accessible by ADMIN users.
     */
    @PostMapping("/categories/edit/{id}")
    public String updateCategory(@PathVariable int id, Model model, @ModelAttribute Category category,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            return "redirect:/index";
        }
        // Handle image upload if a new file is provided
        if (imageFile != null && !imageFile.isEmpty()) {
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
                category.setCategoryImage("/product-images/" + fileName);
            } catch (IOException e) {
                e.printStackTrace(); // Log error for debugging
            }
        }
        categoryService.updateCategory(id, category);
        return "redirect:/admincategories";
    }

    /**
     * Deletes a category by its ID. Only accessible by ADMIN users.
     */
    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if user has ADMIN role
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/index"; // or any error page
        }

        // Get the category name before deleting
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            String categoryName = category.getCategoryName();
            // Delete all products with this category name
            List<Product> products = productService.getAllProductsByCategoryName(categoryName);
            for (Product product : products) {
                productService.deleteProductById(product.getProductId());
            }
        }
        categoryService.deleteCategoryById(id);
        return "redirect:/admincategories";
    }
}

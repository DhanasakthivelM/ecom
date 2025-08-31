package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.model.Category;
import com.example.service.CategoryService;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Displays the home page with a list of all categories for authenticated users.
     * Redirects to login if the user is not authenticated.
     */
    @GetMapping("/home")
    public String home(Model model) {
        List<Category> categories = categoryService.getAllCategories();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/req/login"; // Redirect to login page if not authenticated
        }
        model.addAttribute("categoryList", categories);
        model.addAttribute("user", authentication.getName());
        return "home";
    }
}

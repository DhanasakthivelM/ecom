package com.example.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {
    /**
     * Displays the login page.
     */
    @GetMapping("/req/login")
    public String login() {
        return "login";
    }

    /**
     * Displays the signup page.
     */
    @GetMapping("/req/signup")
    public String signup() {
        return "signup";
    }

    /**
     * Redirects the root URL to the index page.
     */
    @GetMapping("/")
    public String redirectToIndex() {
        return "redirect:/index";
    }

    /**
     * Displays the index (landing) page.
     */
    @GetMapping("/index")
    public String showIndexPage() {
        return "index"; // This should match your index.html in templates
    }

    /**
     * Displays the success page after an operation.
     */
    @GetMapping("/success")
    public String success() {
        return "success";
    }

    /**
     * Displays the about page and adds the current user's name to the model.
     */
    @GetMapping("/about")
    public String about(Model model, Principal principal) {
        model.addAttribute("user", principal.getName());
        return "about";
    }
}

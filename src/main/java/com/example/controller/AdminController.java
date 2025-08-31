package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.service.UserrService;

@Controller
public class AdminController {

	@Autowired
	UserrService us;

	/**
	 * Displays the admin homepage. Only accessible by authenticated users.
	 * Adds the admin's username to the model for display.
	 */
	@GetMapping("/adminhomepage")
	public String adminHome(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return "redirect:/req/login/error?"; // Redirect to login page if not authenticated
		}
		String admin = authentication.getName();
		model.addAttribute("admin", admin);
		return "adminhomepage";
	}

	/**
	 * Displays the list of all users for the admin. Only accessible by
	 * authenticated users.
	 * Adds the user list to the model for display.
	 */
	@GetMapping("/adminusers")
	public String getAllUsers(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return "redirect:/req/login"; // Redirect to login page if not authenticated
		}
		model.addAttribute("userList", us.getAllUsers());
		return "adminusers";
	}

}

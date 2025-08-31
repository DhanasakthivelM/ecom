package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.Userr;
import com.example.repository.UserRepository;
import com.example.model.Role;

@Controller
public class RegisterController {

	@Autowired
	UserRepository ur;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Handles user registration. Checks for duplicate username, encodes password,
	 * and saves user.
	 */
	@PostMapping("/req/signup")
	public String createUserr(@ModelAttribute Userr userr, RedirectAttributes ra) {

		// Check if the username or email already exists to prevent duplicates
		if (ur.findByUsername(userr.getUsername()).isPresent()) {
			ra.addFlashAttribute("error", "Username or email already exists.");
			return "redirect:/req/signup";
		}

		System.out.println("userdetails " + userr); // Debugging line

		userr.setRole(Role.USER); 
		userr.setPassword(passwordEncoder.encode(userr.getPassword()));

		ur.save(userr);

		ra.addFlashAttribute("success", "Signup successful! Please log in.");
		return "redirect:/req/login";
	}
}
package com.example.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Userr;
import com.example.service.UserService;

@Controller
public class UserrController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    /**
     * Adds user details to the model for all requests if the user is authenticated.
     */
    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String username = p.getName();
            // Handle empty Optional gracefully
            Optional<Userr> userOptional = userService.getUserDetails(username);
            if (userOptional.isPresent()) {
                m.addAttribute("user", userOptional.get());
            }
        }
    }

    /**
     * Displays the admin profile page.
     */
    @GetMapping("/adminprofile")
    public String getProfile() {
        return "adminprofile";
    }

    /**
     * Updates the admin profile, including password change if provided.
     */
    @PostMapping("/adminprofile/update")
    public String updateProfile(@ModelAttribute Userr updatedUser,
            @RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmNewPassword,
            Model m) {

        Optional<Userr> userOptional = userService.getUserById(updatedUser.getId());

        if (userOptional.isEmpty()) {
            m.addAttribute("error", "User not found.");
            return "adminprofile";
        }

        Userr existingUser = userOptional.get();

        if (currentPassword != null && !currentPassword.isEmpty()) {
            if (!passwordEncoder.matches(currentPassword, existingUser.getPassword())) {
                m.addAttribute("passwordError", "Incorrect current password.");
                return "adminprofile";
            }
            if (!newPassword.equals(confirmNewPassword)) {
                m.addAttribute("passwordError", "New password and confirmation do not match.");
                return "adminprofile";
            }
            if (newPassword.isEmpty()) {
                m.addAttribute("passwordError", "New password cannot be empty.");
                return "adminprofile";
            }
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());

        Userr savedUser = userService.saveUser(existingUser);

        // Update the security context with the new username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                savedUser.getUsername(),
                savedUser.getPassword(),
                authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return "redirect:/adminprofile?success=true";

    }

    /**
     * Displays the user profile page.
     */
    @GetMapping("/userprofile")
    public String getUserProfile() {
        return "userprofile";
    }

    /**
     * Updates the user profile, including password change if provided.
     */
    @PostMapping("/userprofile/update")
    public String updateUserProfile(@ModelAttribute Userr updatedUser,
            @RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmNewPassword,
            Model m) {

        Optional<Userr> userOptional = userService.getUserById(updatedUser.getId());

        if (userOptional.isEmpty()) {
            m.addAttribute("error", "User not found.");
            return "userprofile";
        }

        Userr existingUser = userOptional.get();
        m.addAttribute("user", existingUser);

        if (currentPassword != null && !currentPassword.isEmpty()) {
            if (!passwordEncoder.matches(currentPassword, existingUser.getPassword())) {
                m.addAttribute("passwordError", "Incorrect current password.");
                return "userprofile";
            }
            if (!newPassword.equals(confirmNewPassword)) {
                m.addAttribute("passwordError", "New password and confirmation do not match.");
                return "userprofile";
            }
            if (newPassword.isEmpty()) {
                m.addAttribute("passwordError", "New password cannot be empty.");
                return "userprofile";
            }
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());

        Userr savedUser = userService.saveUser(existingUser);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                savedUser.getUsername(),
                savedUser.getPassword(),
                authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/userprofile?success=true";
    }
}
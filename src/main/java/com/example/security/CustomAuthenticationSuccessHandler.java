package com.example.security;

import java.io.IOException;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles successful authentication events and redirects users based on their
 * roles.
 * <ul>
 * <li>ROLE_ADMIN: /adminhomepage</li>
 * <li>ROLE_USER: /home</li>
 * <li>Others: /index</li>
 * </ul>
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Redirects users to appropriate pages after successful authentication based on
     * their roles.
     *
     * @param request        HTTP request
     * @param response       HTTP response
     * @param authentication Authentication object containing user details
     * @throws IOException      if an input or output error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        System.out.println("User roles:===================>" + roles);
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/adminhomepage");
        } else if (roles.contains("ROLE_USER")) {
            response.sendRedirect("/home");
        } else {
            response.sendRedirect("/index");
        }
    }
}

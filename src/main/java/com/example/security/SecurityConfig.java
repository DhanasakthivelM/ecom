package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.service.UserService;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

	@Autowired
	private CustomAuthenticationSuccessHandler successHandler;

	private final UserService userService;

	/**
	 * Constructor for injecting UserService dependency.
	 */
	@Autowired
	public SecurityConfig(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Configures the password encoder to use BCrypt hashing.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configures the security filter chain for HTTP security.
	 * Sets up CSRF, authorization rules, login, and logout handling.
	 * Registers the UserDetailsService and PasswordEncoder directly to avoid
	 * deprecated provider usage.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> {
					auth.requestMatchers("/", "/index", "/req/signup", "/css/**", "/js/**").permitAll();
					auth.requestMatchers("/admin/**").hasRole("ADMIN");
					auth.requestMatchers("/user/**").hasRole("USER");
					auth.anyRequest().authenticated();
				})
				.formLogin(form -> {
					form.loginPage("/req/login").permitAll();
					form.successHandler(successHandler);
				})
				.logout(logout -> {
					logout.logoutUrl("/logout")
							.logoutSuccessUrl("/index")
							.permitAll();
				})
				.userDetailsService(userService);
		return http.build();
	}
}

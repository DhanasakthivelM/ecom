package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Userr;
import com.example.repository.UserRepository;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserrService {
	@Autowired
	UserRepository userRepo;

	/**
	 * Retrieves a user by their username.
	 * 
	 * @param username Username to search for
	 * @return Optional containing Userr if found, otherwise empty
	 */
	public Optional<Userr> getUserByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	/**
	 * Retrieves all users from the database.
	 * 
	 * @return List of all Userr objects
	 */
	public List<Userr> getAllUsers() {
		return userRepo.findAll();
	}
}

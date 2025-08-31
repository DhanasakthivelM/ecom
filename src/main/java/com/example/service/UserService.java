package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.model.Userr;
import com.example.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	UserRepository ur;

	/**
	 * Loads a user by username for Spring Security authentication.
	 * 
	 * @param username Username to search for
	 * @return UserDetails object for authentication
	 * @throws UsernameNotFoundException if user is not found
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Userr> user = ur.findByUsername(username);
		if (user.isPresent()) {
			Userr userObj = user.get();
			return User.builder()
					.username(userObj.getUsername())
					.password(userObj.getPassword())
					.roles(userObj.getRole().toString())
					.build();
		} else {
			throw new UsernameNotFoundException(username);
		}
	}

	/**
	 * Retrieves user details by username.
	 * 
	 * @param username Username to search for
	 * @return Optional containing Userr if found, otherwise empty
	 */
	public Optional<Userr> getUserDetails(String username) {
		return ur.findByUsername(username);
	}

	/**
	 * Saves or updates a user in the database.
	 * 
	 * @param existingUser Userr object to save
	 * @return Saved Userr object
	 */
	public Userr saveUser(Userr existingUser) {
		return ur.save(existingUser);
	}

	/**
	 * Retrieves a user by their ID.
	 * 
	 * @param id User ID
	 * @return Optional containing Userr if found, otherwise empty
	 */
	public Optional<Userr> getUserById(int id) {
		return ur.findById(id);
	}
}

package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Userr;

@Repository
public interface UserRepository extends JpaRepository<Userr, Integer> {
	Optional<Userr> findByUsername(String username);

}

package com.example.emplyee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emplyee.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    //Admin findByUsername(String username);
	 Optional<Admin> findByUsername(String username);
	 List<Admin> findByUsernameContainingIgnoreCase(String username);

}
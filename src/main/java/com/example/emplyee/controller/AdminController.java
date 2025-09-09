package com.example.emplyee.controller;

import com.example.emplyee.model.Admin;
import com.example.emplyee.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Show the registration form
    @GetMapping("/register")
    public String showRegistrationForm(Admin admin) {
        return "admin-register"; // This should be a Thymeleaf/HTML template
    }

    // Handle registration form submission
    @PostMapping("/register-admin")
    public String registerAdmin(@ModelAttribute Admin admin, RedirectAttributes redirectAttributes) {
        if (adminRepository.findByUsername(admin.getUsername()).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username already exists!");
            return "redirect:/register";
        }

        // Encrypt the password before saving
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);

        redirectAttributes.addFlashAttribute("successMessage", "Admin registered successfully!");
        return "redirect:/login";
    }
}

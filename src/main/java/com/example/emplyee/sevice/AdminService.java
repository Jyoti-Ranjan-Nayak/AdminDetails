package com.example.emplyee.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.emplyee.model.Admin;
import com.example.emplyee.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createAdmin(Admin admin) {
        // Encrypt the raw password before saving
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);

        // Save the admin to the database
        adminRepository.save(admin);
    }
}

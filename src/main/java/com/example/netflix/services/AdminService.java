package com.example.netflix.services;

import com.example.netflix.models.Admin;
import com.example.netflix.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create new admin with hashed password
    public Admin createAdmin(Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(hashedPassword);
        return adminRepository.save(admin);
    }

    // Get admin by ID
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
    }

    // Get admin by email
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));
    }

    // Check if email exists
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    // Update admin details
    public Admin updateAdmin(Long id, Admin updatedAdmin) {
        Admin existingAdmin = getAdminById(id);

        if (updatedAdmin.getFirstName() != null && !updatedAdmin.getFirstName().isEmpty()) {
            existingAdmin.setFirstName(updatedAdmin.getFirstName());
        }

        if (updatedAdmin.getLastName() != null && !updatedAdmin.getLastName().isEmpty()) {
            existingAdmin.setLastName(updatedAdmin.getLastName());
        }

        if (updatedAdmin.getEmail() != null && !updatedAdmin.getEmail().isEmpty()) {
            if (!existingAdmin.getEmail().equals(updatedAdmin.getEmail()) &&
                    adminRepository.existsByEmail(updatedAdmin.getEmail())) {
                throw new RuntimeException("Email already in use");
            }
            existingAdmin.setEmail(updatedAdmin.getEmail());
        }

        if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(updatedAdmin.getPassword());
            existingAdmin.setPassword(hashedPassword);
        }

        return adminRepository.save(existingAdmin);
    }

    // Delete admin
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    // Authentication methods
    public boolean authenticateAdmin(String email, String password) {
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            return passwordEncoder.matches(password, admin.getPassword());
        }
        return false;
    }

    // Cookie generation and validation
    public String generateAdminCookie(String email) {
        String cookieData = email + ":" + System.currentTimeMillis();
        return Base64.getEncoder().encodeToString(cookieData.getBytes());
    }

    public boolean validateAdminCookie(String cookieValue) {
        try {
            String decoded = new String(Base64.getDecoder().decode(cookieValue));
            String email = decoded.split(":")[0];
            return adminRepository.findByEmail(email).isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    // Additional helper methods
    public void changePassword(Long adminId, String oldPassword, String newPassword) {
        Admin admin = getAdminById(adminId);

        if (!passwordEncoder.matches(oldPassword, admin.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        admin.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(admin);
    }
}
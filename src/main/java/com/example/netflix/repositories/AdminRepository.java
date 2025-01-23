package com.example.netflix.repositories;

import com.example.netflix.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Custom query to find an admin by email
    Optional<Admin> findByEmail(String email);

    // Custom query to check if an admin exists by email
    boolean existsByEmail(String email);
}
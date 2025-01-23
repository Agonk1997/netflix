package com.example.netflix.configs;

import com.example.netflix.models.Admin;
import com.example.netflix.repositories.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.initialize-default-admin:false}")
    private boolean initializeDefaultAdmin;

    public DatabaseInitializer(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (initializeDefaultAdmin) {
            initializeDefaultAdminAccount();
        } else {
            logger.info("Default admin initialization is disabled");
        }
    }

    private void initializeDefaultAdminAccount() {
        long adminCount = adminRepository.count();

        if (adminCount == 0) {
            try {
                Admin defaultAdmin = new Admin();
                defaultAdmin.setFirstName("System");
                defaultAdmin.setLastName("Administrator");
                defaultAdmin.setEmail("admin@netflix.com");
                defaultAdmin.setPassword(passwordEncoder.encode("TempAdmin123!"));

                adminRepository.save(defaultAdmin);
                logger.info("Created default admin account: admin@netflix.com");

            } catch (Exception e) {
                logger.error("Failed to create default admin account", e);
            }
        } else {
            logger.info("Admin accounts already exist. Skipping default admin creation.");
        }
    }
}
package com.example.netflix.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin") // Explicitly specify the table name
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // No need for @Min validation on ID, as it's auto-generated

    @Column(nullable = false, length = 50)
    @Size(max = 50, message = "First name must be less than or equal to 50 characters")
    @NotBlank(message = "First name is required")
    private String firstName; // @NotNull is redundant with @NotBlank

    @Column(nullable = false, length = 50)
    @Size(max = 50, message = "Last name must be less than or equal to 50 characters")
    @NotBlank(message = "Last name is required")
    private String lastName; // @NotNull is redundant with @NotBlank

    @Column(nullable = false, length = 50, unique = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email; // Removed @Pattern, as @Email already validates the format

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password; // @NotNull is redundant with @NotBlank
}
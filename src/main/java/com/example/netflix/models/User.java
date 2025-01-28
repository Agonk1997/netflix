package com.example.netflix.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0, message = "Id must be greater than 0")
    private long id;



    @Column(nullable = false,length = 50)
    @Size(max = 50)
    @NotBlank(message = "First name is required")
    @NotNull(message = "First name is required")
    private String firstName;


    @Column(nullable = false,length = 50)
    @Size(max = 50)
    @NotBlank(message = "Last name is required")
    @NotNull(message = "Last name is required")
    private String lastName;


    @Column(nullable = false,length = 50,unique = true)
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^(.+)@(.+)$", message = "Email format is invalid")

    private String email;


    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @NotNull(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}

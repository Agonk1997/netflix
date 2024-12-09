package com.example.netflix.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title should not exceed 100 characters")
    @Column(nullable = false, length = 100) // Matches database column size
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description should not exceed 500 characters")
    @Column(nullable = false, length = 500) // Matches database column size
    private String description;

    @NotBlank(message = "Image URL is required")
    @Size(max = 1000, message = "Image URL should not exceed 1000 characters")
    @Column(nullable = false, length = 1000) // Increased length for long URLs
    private String imgSrc;

    @NotBlank(message = "Trailer URL is required")
    @Size(max = 1000, message = "Trailer URL should not exceed 1000 characters")
    @Column(nullable = false, length = 1000) // Increased length for long URLs
    private String trailerUrl;

    @NotBlank(message = "Movie URL is required")
    @Size(max = 1000, message = "Movie URL should not exceed 1000 characters")
    @Column(nullable = false, length = 1000) // Increased length for long URLs
    private String movieUrl;
}

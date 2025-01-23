package com.example.netflix.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies") // Ensures the table name in the database is "movies"
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title should not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description should not exceed 500 characters")
    @Column(nullable = false, length = 500)
    private String description;

    @NotBlank(message = "Image URL is required")
    @Size(max = 1000, message = "Image URL should not exceed 1000 characters")

    @Column(nullable = false, length = 1000)
    private String imgSrc;

    @NotBlank(message = "Trailer URL is required")
    @Size(max = 1000, message = "Trailer URL should not exceed 1000 characters")

    @Column(nullable = false, length = 1000)
    private String trailerUrl;

    @NotBlank(message = "Movie file path is required")
    @Size(max = 1000, message = "Movie file path should not exceed 1000 characters")
    @Column(name = "movie_file", nullable = false, length = 1000)
    private String movieFilePath;

//    @NotBlank(message = "Movie URL is required")
//    @Size(max = 1000, message = "Movie URL should not exceed 1000 characters")
//
//    @Column(name = "movie_url", nullable = false, length = 1000)
//    private String movieUrl;

    @Size(max = 255, message = "Movie file name should not exceed 255 characters")
    @Column(name = "movie_file_name", length = 255)
    private String movieFileName;

    // No need to explicitly declare getters and setters if you're using Lombok's @Data
}

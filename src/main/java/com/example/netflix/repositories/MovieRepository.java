package com.example.netflix.repositories;

import com.example.netflix.MovieCategory;
import com.example.netflix.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleContainingIgnoreCase(String query);
    List<Movie> findByCategory(MovieCategory category); // Merr filmat sipas kategorisë
}

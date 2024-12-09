package com.example.netflix.services;

import com.example.netflix.models.Movie;
import com.example.netflix.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // Add a movie
    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // Retrieve all movies
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Retrieve a movie by ID
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    // Update a movie
    public Movie updateMovie(Long id, Movie updatedMovie) {
        Movie existingMovie = getMovieById(id);
        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setDescription(updatedMovie.getDescription());
        existingMovie.setImgSrc(updatedMovie.getImgSrc());
        existingMovie.setTrailerUrl(updatedMovie.getTrailerUrl());
        existingMovie.setMovieUrl(updatedMovie.getMovieUrl());
        return movieRepository.save(existingMovie);
    }

    // Delete a movie
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}

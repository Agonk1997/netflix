package com.example.netflix.services;

import com.example.netflix.MovieCategory;
import com.example.netflix.models.Movie;
import com.example.netflix.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    public Movie updateMovie(Long id, Movie updatedMovie, MultipartFile movieFile) {
        Movie existingMovie = getMovieById(id);
        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setDescription(updatedMovie.getDescription());
        existingMovie.setImgSrc(updatedMovie.getImgSrc());
        existingMovie.setTrailerUrl(updatedMovie.getTrailerUrl());
        existingMovie.setCategory(updatedMovie.getCategory());

        if (!movieFile.isEmpty()) {
            try {
                String fileName = movieFile.getOriginalFilename();
                Path path = Paths.get("uploads/" + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, movieFile.getBytes());

                existingMovie.setMovieFilePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to upload movie file");
            }
        }

        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public List<Movie> searchMovies(String query) {
        return movieRepository.findByTitleContainingIgnoreCase(query);
    }

    public void save(Movie movie) {
        movieRepository.save(movie);
    }

    public List<Movie> getMoviesByCategory(MovieCategory category) {
        return movieRepository.findByCategory(category);
    }
}
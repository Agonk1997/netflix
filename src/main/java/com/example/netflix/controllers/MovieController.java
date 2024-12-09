package com.example.netflix.controllers;

import com.example.netflix.models.Movie;
import com.example.netflix.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Show the form to add a new movie
    @GetMapping("/movies")
    public String viewMovies(Model model) {
        List<Movie> movies = movieService.getAllMovies(); // Ensure this fetches all movies from the database
        model.addAttribute("movies", movies);
        return "movies"; // Corresponds to movies.html
    }


    // Handle the form submission to add a new movie
    @PostMapping("/add")
    public String addMovie(@Validated @ModelAttribute("movie") Movie movie, Model model) {
        movieService.addMovie(movie);
        model.addAttribute("successMessage", "Movie added successfully!");
        return "add-movie";
    }
}

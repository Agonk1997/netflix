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


    @GetMapping("")
    public String getAllMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies"; // Ensure this matches your movies.html template path
    }

    // Show the form to add a new movie
    @GetMapping("/add")
    public String showAddMovieForm(Model model) {
        model.addAttribute("movie", new Movie()); // Bind an empty movie object to the form
        return "add-movie"; // Corresponds to add-movie.html
    }


    // Handle the form submission to add a new movie
    @PostMapping("/add")
    public String addMovie(@Validated @ModelAttribute("movie") Movie movie, Model model) {
        movieService.addMovie(movie);
        model.addAttribute("successMessage", "Movie added successfully!");
        return "add-movie";
    }
}

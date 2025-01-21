package com.example.netflix.controllers;

import com.example.netflix.models.Movie;
import com.example.netflix.services.MovieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    public String getAllMovies(Model model, HttpServletRequest request) {
        // Get the cookie from the request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    String userId = cookie.getValue();
                    // Use the user ID to fetch movies
                    model.addAttribute("movies", movieService.getAllMovies());
                    return "movies";
                }
            }
        }
        // If no cookie is found, redirect to login page
        return "redirect:/login";
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

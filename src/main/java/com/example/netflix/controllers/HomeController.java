package com.example.netflix.controllers;

import com.example.netflix.models.Movie;
import com.example.netflix.models.User;
import com.example.netflix.services.MovieService;
import com.example.netflix.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;
    private final MovieService movieService;

    public HomeController(UserService userService, MovieService movieService) {
        this.userService = userService;
        this.movieService = movieService;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/tv-shows")
    public String tvShows() {
        return "tv-shows";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String query,
            Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        Logger logger = LoggerFactory.getLogger(HomeController.class);

        // Get the cookie from the request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    String userId = cookie.getValue();

                    // Perform the search
                    List<Movie> movies;
                    try {
                        movies = movieService.searchMovies(query);
                        logger.info("Number of movies found for query '{}': {}", query, movies.size()); // Logging
                    } catch (Exception e) {
                        logger.error("Error searching movies: {}", e.getMessage());
                        movies = Collections.emptyList(); // Fallback to empty list
                    }

                    // Add movies and query to the model
                    model.addAttribute("movies", movies != null ? movies : Collections.emptyList());
                    model.addAttribute("query", query); // Pass the query back to the view
                    return "search";
                }
            }
        }

        // If no cookie is found, redirect to login page with a message
        redirectAttributes.addFlashAttribute("error", "Please log in to perform a search.");
        return "redirect:/login";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String newUser(
            @Valid @ModelAttribute User user,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "/sign-up";
        }

        // Check if the email already exists
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("errorMessage", "This email is already registered. Please use a different email.");
            return "/sign-up";
        }

        // Save the user if no errors
        userService.addUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Account created successfully! Please log in.");
        return "redirect:/login";
    }



}
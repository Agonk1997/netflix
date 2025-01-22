package com.example.netflix.controllers;


import com.example.netflix.models.Movie;
import com.example.netflix.models.User;
import com.example.netflix.services.MovieService;
import com.example.netflix.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
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


    public HomeController(UserService userService, MovieService movieService, MovieService movieService1) {
        this.userService = userService;

        this.movieService = movieService1;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }


    @GetMapping("/recently-added")
    public String recentlyAdded() {
        return "recently-added";
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
        Logger logger = LoggerFactory.getLogger(MovieController.class);

        // Get the cookie from the request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    String userId = cookie.getValue();

                    // Optional: Validate the userId (e.g., check if it exists in the database)
                    // if (!userService.isValidUserId(userId)) {
                    //     redirectAttributes.addFlashAttribute("error", "Invalid user ID. Please log in again.");
                    //     return "redirect:/login";
                    // }

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
    public String newUser(@Valid @ModelAttribute User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(System.out::println);
            return "/sign-up";
        }
        userService.addUser(user);
        redirectAttributes.addAttribute("error", "true");

        return "redirect:/login";

    }


}

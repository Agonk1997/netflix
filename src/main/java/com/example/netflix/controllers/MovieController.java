package com.example.netflix.controllers;

import com.example.netflix.models.Movie;
import com.example.netflix.repositories.MovieRepository;
import com.example.netflix.services.MovieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("")
    public String getAllMovies(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    String userId = cookie.getValue();

                    // Fetch movies (logic can be adjusted based on user ID if needed)
                    List<Movie> movies = movieService.getAllMovies();
                    System.out.println("Number of movies found: " + movies.size());

                    model.addAttribute("movies", movies != null ? movies : new ArrayList<>());
                    return "movies"; // Render movies.html
                }
            }
        }

        // If no valid cookie is found, redirect to login page
        return "redirect:/login";
    }




    @GetMapping("/{id}")
    public String getMovieById(@PathVariable("id") long id, Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    String userId = cookie.getValue();

                    // Fetch the movie by ID
                    Movie movie = movieRepository.findById(id).orElse(null);
                    if (movie == null) {
                        return "redirect:/movies"; // Redirect to movies list if the movie is not found
                    }
                    model.addAttribute("movie", movie);
                    return "movie-details"; // Render movie-details.html
                }
            }
        }
        // If no valid cookie is found, redirect to login page
        return "redirect:/login";
    }

}

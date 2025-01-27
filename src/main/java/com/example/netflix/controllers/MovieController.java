package com.example.netflix.controllers;

import com.example.netflix.MovieCategory;
import com.example.netflix.models.Movie;

import com.example.netflix.services.MovieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Shfaq të gjitha filmat
    @GetMapping("")
    public String getAllMovies(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    String userId = cookie.getValue();

                    // Merr të gjitha filmat
                    List<Movie> movies = movieService.getAllMovies();
                    model.addAttribute("movies", movies);
                    model.addAttribute("categories", MovieCategory.values()); // Shto kategoritë në model
                    return "movies"; // Kthen pamjen movies.html
                }
            }
        }

        // Nëse nuk ka cookie të vlefshme, ridrejto në faqen e login
        return "redirect:/login";
    }

    // Shfaq filmat sipas kategorisë
    @GetMapping("/category/{category}")
    public String getMoviesByCategory(@PathVariable("category") MovieCategory category, Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    String userId = cookie.getValue();

                    // Merr filmat sipas kategorisë
                    List<Movie> movies = movieService.getMoviesByCategory(category);
                    model.addAttribute("movies", movies);
                    model.addAttribute("category", category);
                    model.addAttribute("categories", MovieCategory.values()); // Shto kategoritë në model
                    return "movies"; // Kthen pamjen movies.html
                }
            }
        }

        // Nëse nuk ka cookie të vlefshme, ridrejto në faqen e login
        return "redirect:/login";
    }

    // Shfaq detajet e një filmi sipas ID
    @GetMapping("/{id}")
    public String getMovieById(@PathVariable("id") long id, Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    String userId = cookie.getValue();

                    // Merr filmin sipas ID
                    Movie movie = movieService.getMovieById(id);
                    if (movie == null) {
                        return "redirect:/movies"; // Ridrejto nëse filmi nuk gjendet
                    }
                    model.addAttribute("movie", movie);
                    return "movie-details"; // Kthen pamjen movie-details.html
                }
            }
        }

        // Nëse nuk ka cookie të vlefshme, ridrejto në faqen e login
        return "redirect:/login";
    }
}
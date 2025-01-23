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

    @GetMapping("/add")
    public String showAddMovieForm(Model model) {
        model.addAttribute("movie", new Movie()); // Bind an empty movie object to the form
        return "add-movie"; // Render add-movie.html
    }

    @PostMapping("/add")
    public String addMovie(@ModelAttribute Movie movie,
                           @RequestParam("movieFile") MultipartFile movieFile,
                           Model model) {
        try {
            // Validate file input
            if (movieFile.isEmpty()) {
                model.addAttribute("errorMessage", "Movie file is required.");
                return "add-movie";
            }

            // Define the upload directory
            String uploadDir = "src/main/resources/static/uploads/"
                    + movie.getTitle().replaceAll("[^a-zA-Z0-9]", "_") + "/";
            Path uploadPath = Paths.get(uploadDir);

            // Create directory if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file
            String fileName = movieFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, movieFile.getBytes());

            // Set relative path for web access
            movie.setMovieFilePath("/uploads/"
                    + movie.getTitle().replaceAll("[^a-zA-Z0-9]", "_") + "/" + fileName);

            // Save the movie to the database
            movieRepository.save(movie);
            return "redirect:/movies";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error occurred while uploading the file.");
            return "add-movie";
        }
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

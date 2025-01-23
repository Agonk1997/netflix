package com.example.netflix.controllers;

import com.example.netflix.models.Movie;
import com.example.netflix.repositories.MovieRepository;
import com.example.netflix.services.AdminService;
import com.example.netflix.services.MovieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MovieRepository movieRepository;

    private final AdminService adminService;
    private final MovieService movieService;
    private static final String ADMIN_COOKIE_NAME = "adminAuth";

    public AdminController(AdminService adminService, MovieService movieService) {
        this.adminService = adminService;
        this.movieService = movieService;
    }

    // ================= LOGIN =================
    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request) {
        if (isAdminAuthenticated(request)) {
            return "redirect:/admin/dashboard";
        }
        return "admin/login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        if (adminService.authenticateAdmin(email, password)) {
            // Create secure cookie
            Cookie adminCookie = new Cookie(ADMIN_COOKIE_NAME, adminService.generateAdminCookie(email));
            adminCookie.setMaxAge(24 * 60 * 60); // 24 hours
            adminCookie.setHttpOnly(true);
            adminCookie.setPath("/admin");
            response.addCookie(adminCookie);

            return "redirect:/admin/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/admin/login";
        }
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String showDashboard(HttpServletRequest request) {
        if (!isAdminAuthenticated(request)) {
            return "redirect:/admin/login";
        }

        return "admin/dashboard";
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public String processLogout(HttpServletResponse response) {
        // Invalidate the cookie
        Cookie cookie = new Cookie(ADMIN_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/admin");
        response.addCookie(cookie);

        return "redirect:/admin/login";
    }

    // ================= HELPER METHODS =================
    private boolean isAdminAuthenticated(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(ADMIN_COOKIE_NAME)) {
                    return adminService.validateAdminCookie(cookie.getValue());
                }
            }
        }
        return false;
    }
    @GetMapping("/add")
    public String showAddMovieForm(HttpServletRequest request, Model model) {
        if (!isAdminAuthenticated(request)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("movie", new Movie());
        return "admin/add-movie";
    }

    @PostMapping("/add")
    public String addMovie(
            @ModelAttribute Movie movie,
            @RequestParam("movieFile") MultipartFile movieFile,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!isAdminAuthenticated(request)) {
            return "redirect:/admin/login";
        }

        try {
            // Validate file input
            if (movieFile.isEmpty()) {
                model.addAttribute("errorMessage", "Movie file is required.");
                return "admin/add-movie";
            }

            // Sanitize movie title for directory name
            String sanitizedTitle = movie.getTitle().replaceAll("[^a-zA-Z0-9]", "_");
            String uploadDir = "src/main/resources/static/uploads/" + sanitizedTitle + "/";
            Path uploadPath = Paths.get(uploadDir);

            // Create directory if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String fileName = System.currentTimeMillis() + "_" + movieFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, movieFile.getBytes());

            // Set relative path for web access
            movie.setMovieFilePath("/uploads/" + sanitizedTitle + "/" + fileName);

            // Save the movie to the database
            movieRepository.save(movie);

            redirectAttributes.addFlashAttribute("success", "Movie added successfully!");
            return "redirect:/admin/dashboard";

        } catch (IOException e) {
            model.addAttribute("errorMessage", "File upload error: " + e.getMessage());
            return "admin/add-movie";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "admin/add-movie";
        }
    }


}


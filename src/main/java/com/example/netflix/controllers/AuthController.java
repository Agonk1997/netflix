package com.example.netflix.controllers;

import com.example.netflix.models.User;
import com.example.netflix.repositories.UserRepository;
import com.example.netflix.services.UserService;
import com.example.netflix.services.UserServiceLogin;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final UserServiceLogin userServiceLogin;
    private final UserService userService;

    public AuthController(UserRepository userRepository, @Qualifier("userService") UserServiceLogin userServiceLogin, UserService userService) {
        this.userRepository = userRepository;
        this.userServiceLogin = userServiceLogin;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Check if the user is already logged in
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    // User is already logged in, redirect to home page
                    redirectAttributes.addFlashAttribute("message", "You are already logged in.");
                    return "redirect:/index";
                }
            }
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes
    ) {
        // Perform login logic
        var user = userService.login(email, password);
        if (user.isPresent()) {
            // Create a cookie to indicate the user is logged in
            Cookie cookie = new Cookie("userId", String.valueOf(user.get().getId()));
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        // Invalidate the userId cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    cookie.setValue(null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully.");
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
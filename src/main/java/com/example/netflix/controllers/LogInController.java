package com.example.netflix.controllers;

import com.example.netflix.dtos.LoginRequestDto;
import com.example.netflix.models.User;
import com.example.netflix.repositories.UserRepository;
import com.example.netflix.services.UserService; // Import UserService
import com.example.netflix.services.UserServiceLogin;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LogInController {

    private final UserRepository userRepository;
    private UserServiceLogin userServiceLogin = null; // Declare UserService

    // Constructor injection for dependencies
    public LogInController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userServiceLogin = userServiceLogin;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequestDto loginRequestDto,
                        BindingResult bindingResult,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam(value = "returnUrl", required = false) String returnUrl) {
        if (bindingResult.hasErrors()) {
            return "redirect:/movies";
        }

        // Use the injected userService to authenticate the user
        var userDto = userServiceLogin.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        if (userDto == null) {
            bindingResult.rejectValue("email", "error.loginRequestDto", "Emaili ose passwordi i gabuar!");
            bindingResult.rejectValue("password", "error.loginRequestDto", "Emaili ose passwordi i gabuar!");
            return "redirect:/movies";
        }

        // Set session attribute
        HttpSession session = request.getSession();
        session.setAttribute("user", userDto);

        // Default cookie behavior
        Cookie cookie = new Cookie("userId", String.valueOf(userDto.getId()));
        cookie.setMaxAge(60 * 60); // Cookie valid for 1 hour
        response.addCookie(cookie);

        // Redirect to return URL or home page
        if (returnUrl == null || returnUrl.isBlank()) {
            return "redirect:/";
        }
        return "redirect:" + returnUrl;
    }
}

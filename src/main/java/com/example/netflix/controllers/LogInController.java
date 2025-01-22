package com.example.netflix.controllers;

import com.example.netflix.dtos.LoginRequestDto;
import com.example.netflix.models.User;
import com.example.netflix.repositories.UserRepository;
import com.example.netflix.services.UserServiceLogin;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class LogInController {

    private static final Logger logger = LoggerFactory.getLogger(LogInController.class);

    private final UserRepository userRepository;
    private final UserServiceLogin userServiceLogin;

    public LogInController(UserRepository userRepository, @Qualifier("userService") UserServiceLogin userServiceLogin) {
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
                        @RequestParam(value = "returnUrl", required = false) String returnUrl, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Incorrect email or password.");
            return "login";
        }

        logger.info("Attempting to log in user with email: {}", loginRequestDto.getEmail());

        var userDto = userServiceLogin.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        if (userDto == null) {
            logger.warn("Login failed for user with email: {}", loginRequestDto.getEmail());
            bindingResult.rejectValue("email", "error.loginRequestDto", "Incorrect email or password.");
            bindingResult.rejectValue("password", "error.loginRequestDto", "Incorrect email or password.");

            return "login";
        }

        logger.info("Login successful for user with email: {}", loginRequestDto.getEmail());

        HttpSession session = request.getSession();
        session.setAttribute("user", userDto);

        Cookie cookie = new Cookie("userId", String.valueOf(userDto.getId()));
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        if (returnUrl == null || returnUrl.isBlank()) {
            return "redirect:/";
        }

        return "redirect:" + returnUrl;
    }
}
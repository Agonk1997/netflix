package com.example.netflix.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
@Controller
public class HomeControllers {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Netflix");
        model.addAttribute("heroTitle", "See what's next.");
        model.addAttribute("heroDescription", "Watch anywhere. Cancel anytime.");
        model.addAttribute("featureTitle1", "Enjoy on your TV.");
        model.addAttribute("featureDescription1", "Watch on Smart TVs, Playstation, Xbox, Chromecast, Apple TV, Blu-ray players and more.");
        model.addAttribute("featureTitle2", "Download your shows to watch offline.");
        model.addAttribute("featureDescription2", "Save your favorites for later.");
        model.addAttribute("featureTitle3", "Watch everywhere.");
        model.addAttribute("featureDescription3", "Stream on up to 4 screens at the same time.");
        return "index";
    }

    @GetMapping("/sign-in")
    public String signIn() {

        return "redirect:/login";
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "redirect:/register";
    }

    // ... other controller methods
}}
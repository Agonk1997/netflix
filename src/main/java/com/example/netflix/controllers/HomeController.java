package com.example.netflix.controllers;


import com.example.netflix.models.User;
import com.example.netflix.services.MovieService;
import com.example.netflix.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class HomeController {


    private final UserService userService;


    public HomeController(UserService userService, MovieService movieService) {
        this.userService = userService;

    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }


    @GetMapping("/recently-added")
    public String recentlyAdded(){
        return "recently-added";
    }

    @GetMapping("/tv-shows")
    public String tvShows(){
        return "tv-shows";
    }

    @GetMapping("/search")
    public String search(){
        return "search";
    }


    @GetMapping("/sign-up")
    public String signUp(Model model){
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

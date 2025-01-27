package com.example.netflix.controllers;

import com.example.netflix.models.Comment;
import com.example.netflix.models.User;
import com.example.netflix.services.CommentService;
import com.example.netflix.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping("/submitComment")
    public String submitComment(@RequestParam("content") String content, HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        User user = null;

        // Get the user from the cookies
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    user = userService.getUserById(Long.parseLong(cookie.getValue()));
                }
            }
        }

        // If user is not found, redirect to login page
        if (user == null) {
            return "redirect:/login";
        }

        // Create and save the comment
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);

        commentService.saveComment(comment);

        // Add a success message and redirect to the comments page
        model.addAttribute("message", "Comment submitted successfully!");
        return "redirect:/comments";
    }
}

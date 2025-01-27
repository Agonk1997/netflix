package com.example.netflix.controllers;

import com.example.netflix.models.Comment;
import com.example.netflix.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/v1/comments")
public class CommentRestController {

    private final CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Endpoint to fetch all comments
    @GetMapping("/api/v1/comments")
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }
}

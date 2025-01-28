package com.example.netflix.services;

import com.example.netflix.models.Comment;
import com.example.netflix.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // Përdor metodën e re për të marrë komentet së bashku me të dhënat e përdoruesit
    public List<Comment> getAllComments() {
        return commentRepository.findAllWithUser();
    }

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
}
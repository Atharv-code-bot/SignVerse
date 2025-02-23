package com.ISL.ISL.controller;
import com.ISL.ISL.model.Comment;
import com.ISL.ISL.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
        System.out.println("🔥 CommentController Loaded");
    }

    @PostMapping("/{postId}")
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestBody Comment comment) {
        System.out.println("➡️ Received POST request to add comment to postId: " + postId);
        return ResponseEntity.ok(commentService.addComment(postId, comment));
    }



    // ✅ Get all comments for a post
    @GetMapping("/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }
}


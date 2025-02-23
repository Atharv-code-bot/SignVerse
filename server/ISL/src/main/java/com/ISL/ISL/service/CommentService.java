package com.ISL.ISL.service;
import com.ISL.ISL.config.AuthUtil;
import com.ISL.ISL.model.Comment;
import com.ISL.ISL.model.Post;
import com.ISL.ISL.model.User;
import com.ISL.ISL.repository.CommentRepository;
import com.ISL.ISL.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthUtil authUtil;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, AuthUtil authUtil) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.authUtil = authUtil;
    }

    // ✅ Add a comment to a post
    public Comment addComment(Long postId, Comment comment) {
        User user = authUtil.getLoggedInUser();
        if (user == null) {
            throw new RuntimeException("Unauthorized: User not logged in");
        }
        System.out.println("Logged-in User: " + user.getName());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        System.out.println("Post found: " + post.getId());

        comment.setUser(user);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);
        System.out.println("Saved Comment ID: " + savedComment.getId());

        return savedComment;
    }


    // ✅ Get all comments for a post
    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}


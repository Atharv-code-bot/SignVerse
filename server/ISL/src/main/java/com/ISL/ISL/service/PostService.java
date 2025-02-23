package com.ISL.ISL.service;
import com.ISL.ISL.config.AuthUtil;
import com.ISL.ISL.model.Post;
import com.ISL.ISL.model.User;
import com.ISL.ISL.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final AuthUtil authUtil;

    public PostService(PostRepository postRepository, AuthUtil authUtil) {
        this.postRepository = postRepository;
        this.authUtil = authUtil;
    }

    // ✅ Create a new post
    public Post createPost(Post post) {
        User user = authUtil.getLoggedInUser();
        if (user == null) {
            throw new RuntimeException("Unauthorized: User not logged in");
        }
        post.setUser(user);
        return postRepository.save(post);
    }

    // ✅ Get all posts
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}


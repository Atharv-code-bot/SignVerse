package com.ISL.ISL.repository;

import com.ISL.ISL.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

